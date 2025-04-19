package tn.esprit.offre_promotion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest; // Changement de javax à jakarta
import jakarta.servlet.http.HttpServletResponse; // Changement de javax à jakarta

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public SecurityConfig() {
        System.out.println("INFO: SecurityConfig is being initialized");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2AuthorizedClientService authorizedClientService) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/AllOffer").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/keycloak")
                        .successHandler(successHandler(authorizedClientService))
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll()
                );
        return http.build();
    }

    private AuthenticationSuccessHandler successHandler(OAuth2AuthorizedClientService authorizedClientService) {
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) -> {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(),
                    oauthToken.getName()
            );

            if (client != null && client.getAccessToken() != null) {
                String accessToken = client.getAccessToken().getTokenValue();

                // Appeler l'API /AllOffer via le gateway
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8093/offre_promotion/AllOffer"; // URL via le gateway
                org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                headers.set("Authorization", "Bearer " + accessToken);
                org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

                org.springframework.http.ResponseEntity<String> apiResponse = restTemplate.exchange(
                        url,
                        org.springframework.http.HttpMethod.GET,
                        entity,
                        String.class
                );

                // Renvoyer la réponse JSON directement au navigateur
                response.setContentType("application/json");
                response.setStatus(apiResponse.getStatusCodeValue());
                response.getWriter().write(apiResponse.getBody());
                response.getWriter().flush();
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unable to retrieve access token");
            }
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = "http://keycloak:8080/realms/myrealm/protocol/openid-connect/certs";
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> issuerValidator = jwt -> {
            String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
            System.out.println("DEBUG: Issuer reçu = " + issuer);
            if (issuer == null ||
                    (!issuer.equals("http://keycloak:8080/realms/myrealm") &&
                            !issuer.equals("http://localhost:8180/realms/myrealm"))) {
                System.out.println("DEBUG: Issuer invalide = " + issuer);
                return OAuth2TokenValidatorResult.failure(
                        new OAuth2Error("invalid_issuer", "Invalid issuer: " + issuer, null)
                );
            }
            System.out.println("DEBUG: Issuer valide = " + issuer);
            return OAuth2TokenValidatorResult.success();
        };

        OAuth2TokenValidator<Jwt> audienceValidator = jwt -> {
            if (!jwt.getAudience().contains("offre-promotion-client")) {
                System.out.println("DEBUG: Audience invalide = " + jwt.getAudience());
                return OAuth2TokenValidatorResult.failure(
                        new OAuth2Error("invalid_audience", "Invalid audience: " + jwt.getAudience(), null)
                );
            }
            System.out.println("DEBUG: Audience valide = " + jwt.getAudience());
            return OAuth2TokenValidatorResult.success();
        };

        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                issuerValidator,
                audienceValidator
        );

        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }
}