package com.azurer.spring.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义授权管理器
 */
@Slf4j
@Component
public class CustomAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final Set<String> permitAll = ConcurrentHashMap.newKeySet();
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public CustomAuthorizationManager() {
        permitAll.add("/");
        permitAll.add("/error");
        permitAll.add("/favicon.ico");
        permitAll.add("/**/v2/api-docs/**");
        permitAll.add("/**/swagger-resources/**");
        permitAll.add("/webjars/**");
        permitAll.add("/doc.html");
        permitAll.add("/swagger-ui.html");
        permitAll.add("/**/oauth/**");
        permitAll.add("/**/current/get");
        permitAll.add("/provider-service/**");
        permitAll.add("/consumer-service/**");
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        String requestPath = authorizationContext.getExchange().getRequest().getURI().getPath();

        if (permitAll.stream().anyMatch(r -> antPathMatcher.match(r, requestPath))) {
            return Mono.just(new AuthorizationDecision(true));
        }

        return mono.map(auth -> new AuthorizationDecision(checkAuthorities(auth, requestPath)))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private boolean checkAuthorities(Authentication auth, String requestPath) {
        if (auth instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) auth;

            Integer value1 = (Integer) authentication.getOAuth2Request().getExtensions().get("key1");
            String value2 = (String) authentication.getOAuth2Request().getExtensions().get("key2");
            Boolean value3 = (Boolean) authentication.getOAuth2Request().getExtensions().get("key3");
            log.info("key1:{} 2:{} 3:{}", value1, value2, value3);

            Set<String> scope = authentication.getOAuth2Request().getScope();
            for (String str : scope) {
                if (requestPath.startsWith(str)) {
                    log.info("requestPath:{} In scope:{}", requestPath, str);
                    return true;
                }
            }

            Set<String> resourceIds = authentication.getOAuth2Request().getResourceIds();
            if (!resourceIds.contains(requestPath)) {
                log.info("requestPath:{} Not In resourceIds:{}", requestPath, resourceIds);
                return false;
            }
        }
        Object principal = auth.getPrincipal();
        log.info("用户信息:{}", principal.toString());
        return true;
    }
}
