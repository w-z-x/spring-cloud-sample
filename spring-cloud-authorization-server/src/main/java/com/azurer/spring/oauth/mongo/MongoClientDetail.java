package com.azurer.spring.oauth.mongo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Document(collection = "client")
public class MongoClientDetail extends BaseClientDetails {
    @Id
    private String id;
}
