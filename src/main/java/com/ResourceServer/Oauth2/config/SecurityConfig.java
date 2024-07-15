package com.ResourceServer.Oauth2.config;



import com.ResourceServer.Oauth2.filter.CsrfFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Using the Jwt Authentication Converter we are going to configure the Keycloak role converter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // This method is responsible to convert the roles from the JWT token to the grantedAuthorities.
        // It takes the parameter KeycloakRoleConverter
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        // Csrf Configuration
        CsrfTokenRequestAttributeHandler attributeHandler = new CsrfTokenRequestAttributeHandler();
        attributeHandler.setCsrfRequestAttributeName("_csrf");

        // Configure session management for the HTTP security object
        http.sessionManagement(session ->
                // Set the session creation policy to STATELESS
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.csrf(httpSecurityCsrfConfigurer -> {
            httpSecurityCsrfConfigurer.csrfTokenRequestHandler(attributeHandler)
                    .ignoringRequestMatchers("/contact", "/register")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }).addFilterAfter(new CsrfFilter(), BasicAuthenticationFilter.class);

        // Cors Configuration
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                // Since we will send the JWT token as a response header we will need to let our browser know of this.
                corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                // Set the maximum age (in seconds) for which the CORS preflight response should be cached by the client
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            });
        }).requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()).authorizeHttpRequests(requests -> {
            requests.requestMatchers("/notices", "/contact", "/register", "/error").permitAll();
            requests.requestMatchers("/myAccount").hasRole("USER")
                    .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/myLoans").hasRole("USER")
                    .requestMatchers("/myCards").hasRole("USER")
                    .requestMatchers("/user").authenticated();
        });

        http.oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
            // There are two types of tokens: opaque tokens and JWTs
            httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer -> {
                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);
            });
        });

        return http.build();
    }
}
