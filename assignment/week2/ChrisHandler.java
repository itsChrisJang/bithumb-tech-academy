package com.codestates.practice.assignment.week2;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
public class ChrisHandler {
    public Mono<ServerResponse> hello (ServerRequest request) {
        String name = request.queryParam("name").get();
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("to", name);
        returnMap.put("message", "hello " + name);

        //String print = "to : " + name + " hello : " + name;

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(returnMap), HashMap.class);
        /*
            Variant of body(Publisher, Class) that allows using any producer that can be resolved to Publisher via ReactiveAdapterRegistry.
            producer - the producer to write to the request
            elementClass - the type of elements produced
         */
    }
}
