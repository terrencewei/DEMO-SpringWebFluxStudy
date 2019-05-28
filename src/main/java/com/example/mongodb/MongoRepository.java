package com.example.mongodb;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * The media MongoDB repository
 */
public interface MongoRepository extends ReactiveMongoRepository<TestCollection, String> {

}
