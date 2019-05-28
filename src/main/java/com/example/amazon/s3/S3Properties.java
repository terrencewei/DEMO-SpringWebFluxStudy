package com.example.amazon.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = "aws-s3-config")
@Getter
@Setter
@ToString
public class S3Properties {

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;

}