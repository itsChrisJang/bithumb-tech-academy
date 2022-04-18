package com.codestates.practice.assignment.week2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ChrisRouter {
    @Bean
    public RouterFunction<ServerResponse> route(ChrisHandler chrisHandler) {
        RouterFunction<ServerResponse> route =
                RouterFunctions
                        .route()
                        .GET("/hello", RequestPredicates.accept(MediaType.APPLICATION_JSON), chrisHandler::hello)
                        .build();
        return route;
    }
}
