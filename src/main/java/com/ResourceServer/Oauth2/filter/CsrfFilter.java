package com.ResourceServer.Oauth2.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println("Csrf Token Handler " +
                ""+ csrfToken);

        // Check if the CSRF token's header name is not null
        // If the header name is not null inject the values here.
        if (csrfToken != null && csrfToken.getHeaderName() != null) {
            // Set the CSRF token in the response header
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
        // Continue the filter chain with the request and response objects
        filterChain.doFilter(request, response);

    }
}
