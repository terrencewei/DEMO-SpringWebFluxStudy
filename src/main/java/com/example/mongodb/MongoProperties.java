package com.example.mongodb;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = "custom-mongodb-config")
@Getter
@Setter
@ToString
public class MongoProperties {

    @NotNull
    private String        database;
    @NotNull
    private List<String>  hosts;
    @NotNull
    private List<Integer> ports;
    private String        replicaSet;
    private String        username;
    private String        password;
    private String        authenticationDatabase;
    private Integer       connectionPoolMinSize;
    private Integer       connectionPoolMaxSize;
    private Integer       connectionPoolMaxWaitTime;
    private Integer       socketConnectTimeout;
    private Integer       socketReadTimeout;
    private Long          clusterSettingsServerSelectionTimeout;

}