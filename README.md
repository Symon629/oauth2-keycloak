# Dependencies Installed 
Spring-Security

spring-boot-starter-oauth2-resource-server

## After that's installed
 We have a created a new package called config
 And in that we have all the security config
 
```dtd
package com.ResourceServer.Oauth2.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // Csrf Configuration
        CsrfTokenRequestAttributeHandler attributeHandler = new CsrfTokenRequestAttributeHandler();
        attributeHandler.setCsrfRequestAttributeName("_csrf");

        // Configure session management for the HTTP security object
        http.sessionManagement(session ->
                // Set the session creation policy to STATELESS
                // This ensures that the application does not create or use HTTP sessions
                // Each request is independent and contains all the necessary authentication information
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.csrf(httpSecurityCsrfConfigurer -> {
            httpSecurityCsrfConfigurer.csrfTokenRequestHandler(attributeHandler)
                    .ignoringRequestMatchers("/myContact","/register")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        });
        //Cors Configuration

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                // Since we will send the jwt token  as a response header we will need to let our browswer know of this.
                corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                // Set the maximum age (in seconds) for which the CORS preflight response should be cached by the client
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            });
        }).authorizeHttpRequests(requests->{
            requests.requestMatchers("/myAccount").hasRole("USER")
                    .requestMatchers("/myBalance").hasAnyRole("USER","ADMIN")
                    .requestMatchers("/myLoans").hasRole("USER")
                    .requestMatchers("/myCards").hasRole("USER")
                    .requestMatchers("/user").authenticated()
                    .requestMatchers("/notices","/contact","/register").permitAll();
        });

        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();

    }
}

```
### This is how you configure Csrf
```dtd
        // Csrf Configuration
        CsrfTokenRequestAttributeHandler attributeHandler = new CsrfTokenRequestAttributeHandler();
        attributeHandler.setCsrfRequestAttributeName("_csrf");

        // Configure session management for the HTTP security object
        http.sessionManagement(session ->
                // Set the session creation policy to STATELESS
                // This ensures that the application does not create or use HTTP sessions
                // Each request is independent and contains all the necessary authentication information
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.csrf(httpSecurityCsrfConfigurer -> {
            httpSecurityCsrfConfigurer.csrfTokenRequestHandler(attributeHandler)
                    .ignoringRequestMatchers("/myContact","/register")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        });
```
### This is how you configure cors
```dtd
    http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                // Since we will send the jwt token  as a response header we will need to let our browswer know of this.
                corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                // Set the maximum age (in seconds) for which the CORS preflight response should be cached by the client
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            });
        })
```
### This how you configure some request to restrict routes or permit routes
```dtd

http.authorizeHttpRequests(requests->{
            requests.requestMatchers("/myAccount").hasRole("USER")
                    .requestMatchers("/myBalance").hasAnyRole("USER","ADMIN")
                    .requestMatchers("/myLoans").hasRole("USER")
                    .requestMatchers("/myCards").hasRole("USER")
                    .requestMatchers("/user").authenticated()
                    .requestMatchers("/notices","/contact","/register").permitAll();
        });
```
## We enable formlogin() and http Basic authentication()

```dtd
   http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
```

