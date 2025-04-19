package com.example.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/offre_promotion/**").authenticated()
                        .anyExchange().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        // Utiliser le nom correct du service Keycloak dans le réseau Docker
        String jwkSetUri = "http://microservice-keycloak-1:8080/realms/myrealm/protocol/openid-connect/certs";
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Créer un validateur personnalisé pour les issuers
        OAuth2TokenValidator<Jwt> issuerValidator = jwt -> {
            String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
            if (issuer == null || (!issuer.equals("http://microservice-keycloak-1:8080/realms/myrealm") &&
                    !issuer.equals("http://localhost:8180/realms/myrealm"))) {
                OAuth2Error error = new OAuth2Error(
                        "invalid_issuer",
                        "Invalid issuer: " + issuer,
                        null
                );
                return OAuth2TokenValidatorResult.failure(Collections.singletonList(error));
            }
            return OAuth2TokenValidatorResult.success();
        };

        // Combiner le validateur d'issuer avec d'autres validateurs
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                Collections.singletonList(issuerValidator)
        );

        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }
}