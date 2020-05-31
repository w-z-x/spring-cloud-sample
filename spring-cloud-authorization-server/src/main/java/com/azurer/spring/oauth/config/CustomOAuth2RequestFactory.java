package com.azurer.spring.oauth.config;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2RequestFactory extends DefaultOAuth2RequestFactory {

    public CustomOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
    }

    @Override
    public OAuth2Request createOAuth2Request(ClientDetails client, TokenRequest tokenRequest) {
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(client);

        Map<String, Object> additionalInformation = client.getAdditionalInformation();
        Map<String, Serializable> extensionProperties = new HashMap<>();
        for (String key : additionalInformation.keySet()) {
            Object value = additionalInformation.get(key);
            if (value instanceof String) {
                extensionProperties.put(key, (String) value);
            } else if (value instanceof Integer) {
                extensionProperties.put(key, (Integer) value);
            } else if (value instanceof Boolean) {
                extensionProperties.put(key, (Boolean) value);
            }
        }

        return new OAuth2Request(oAuth2Request.getRequestParameters(), oAuth2Request.getClientId(),
                oAuth2Request.getAuthorities(), oAuth2Request.isApproved(), oAuth2Request.getScope(),
                oAuth2Request.getResourceIds(), oAuth2Request.getRedirectUri(), oAuth2Request.getResponseTypes(),
                extensionProperties
        );
    }
}
