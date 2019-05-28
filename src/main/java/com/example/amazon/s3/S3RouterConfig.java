package com.example.amazon.s3;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class S3RouterConfig {

    @Autowired
    private S3RouterHandler s3RouterHandler;



    @Bean
    public RouterFunction<ServerResponse> s3ApiRouter() {
        return route(POST("/s3/upload").and(accept(MediaType.APPLICATION_JSON)), s3RouterHandler::upload2S3)
                .andRoute(GET("/s3/download/{name}").and(accept(MediaType.APPLICATION_JSON)),
                        s3RouterHandler::downloadFromS3)
                .andRoute(GET("/s3/view/{name}").and(accept(MediaType.APPLICATION_JSON)), s3RouterHandler::viewFromS3);
    }
}