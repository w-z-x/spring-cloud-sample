package com.azurer.spring.oauth.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MongoClientDetailsService implements ClientDetailsService {
    @Resource
    MongoClientDetailRepository mongoClientDetailRepository;

    @Override
    @Cacheable("mongo")
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        MongoClientDetail mongoClientDetail = mongoClientDetailRepository.findByClientId(clientId);
        log.info("{}", mongoClientDetail.toString());
        return mongoClientDetail;
    }
}
