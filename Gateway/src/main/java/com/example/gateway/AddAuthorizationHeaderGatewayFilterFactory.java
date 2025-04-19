package com.example.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AddAuthorizationHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<AddAuthorizationHeaderGatewayFilterFactory.Config> {

    public AddAuthorizationHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getHeaders().containsKey("Authorization")) {
                String authHeader = request.getHeaders().getFirst("Authorization");
                System.out.println("Authorization header found: " + authHeader);
                exchange.getRequest().mutate()
                        .header("Authorization", authHeader)
                        .build();
            } else {
                System.out.println("No Authorization header found in the request.");
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuration properties if needed
    }
}