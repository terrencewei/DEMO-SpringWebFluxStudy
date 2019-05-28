package com.example.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom mongo config to support more features
 *
 * @see org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
 * @see org.springframework.boot.autoconfigure.mongo.ReactiveMongoClientFactory
 */
@Configuration
@Slf4j
public class MongoConfig {

    @Autowired
    private MongoProperties mongoProperties;



    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() throws Exception {
        return new ReactiveMongoTemplate(getReactiveMongoDatabaseFactory());
    }



    @Bean
    public ReactiveMongoDatabaseFactory getReactiveMongoDatabaseFactory() throws Exception {
        return new SimpleReactiveMongoDatabaseFactory(getMongoClient(), mongoProperties.getDatabase());

    }



    @Bean
    public MongoClient getMongoClient() {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String host : mongoProperties.getHosts()) {
            Integer index = mongoProperties
                    .getHosts()
                    .indexOf(host);
            Integer port = mongoProperties
                    .getPorts()
                    .get(index);

            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddresses.add(serverAddress);
        }
        log.info("MONGO_DB: --> Using serverAddresses:{}", serverAddresses);

        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(mongoProperties.getUsername(),
                mongoProperties.getAuthenticationDatabase() != null ?
                        mongoProperties.getAuthenticationDatabase() :
                        mongoProperties.getDatabase(), mongoProperties
                        .getPassword()
                        .toCharArray());

        log.info("MONGO_DB: --> Using mongoCredential:{}", mongoCredential);

        MongoClientSettings settings = MongoClientSettings
                .builder()
                .applyToConnectionPoolSettings(pBuilder -> pBuilder
                        .minSize(mongoProperties.getConnectionPoolMinSize())
                        .maxSize(mongoProperties.getConnectionPoolMaxSize())
                        .maxWaitTime(mongoProperties.getConnectionPoolMaxWaitTime(), TimeUnit.SECONDS)
                        .build())
                .applyToSocketSettings(pBuilder -> pBuilder
                        .connectTimeout(mongoProperties.getSocketConnectTimeout(), TimeUnit.SECONDS)
                        .readTimeout(mongoProperties.getSocketReadTimeout(), TimeUnit.SECONDS)
                        .build())
                .credential(mongoCredential)
                .applyToClusterSettings(pBuilder -> pBuilder
                        .hosts(serverAddresses)
                        .serverSelectionTimeout(mongoProperties.getClusterSettingsServerSelectionTimeout(),
                                TimeUnit.SECONDS)
                        .build())
                .build();

        return MongoClients.create(settings);
    }

}