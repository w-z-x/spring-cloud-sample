package com.azurer.spring.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    @Resource
    public RedisConnectionFactory redisConnectionFactory;

    @Resource
    public ClientDetailsService clientDetailsService;

    @Bean
    public TokenStore tokenStore() {
        //使用redis存储token
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //设置redis token存储中的前缀
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //配置token存储
        tokenServices.setTokenStore(tokenStore());
        //开启支持refresh_token
        tokenServices.setSupportRefreshToken(true);
        //token有效期
        tokenServices.setAccessTokenValiditySeconds(1 * 60 * 60);
        //refresh_token有效期
        tokenServices.setRefreshTokenValiditySeconds(1 * 1 * 60 * 60);
        return tokenServices;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenGranter tokenGranter() {
        ClientDetailsService clientDetails = clientDetailsService;
        AuthorizationServerTokenServices tokenServices = tokenServices();
        OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetails);

        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        ClientCredentialsTokenGranter clientCredentialsTokenGranter =
                new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory);
        clientCredentialsTokenGranter.setAllowRefresh(true);
        tokenGranters.add(clientCredentialsTokenGranter);
        return new CompositeTokenGranter(tokenGranters);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //@formatter:off
        clients.inMemory()
                .withClient("client_id_sample")
                .secret(passwordEncoder().encode("client_secret_sample"))
                .authorizedGrantTypes("refresh_token", "client_credentials") //主要配置这里为client_credentials
                .scopes("all");
        //@formatter:on
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenServices(tokenServices())
                .tokenGranter(tokenGranter());
    }
}
