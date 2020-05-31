package com.azurer.spring.oauth.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoClientDetailRepository extends MongoRepository<MongoClientDetail, String> {
    MongoClientDetail findByClientId(String clientId);
}
