package com.chatapk.chatapplication.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.chatapk.chatapplication.services.JwtUtil;

import reactor.core.publisher.Mono;

public class JwtTokenAuthenticationFilter implements WebFilter {

    private final String HEADER_PREFIX = "Bearer ";

    private JwtUtil jwtUtil;

    public JwtTokenAuthenticationFilter() {
    }

    public JwtTokenAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!exchange.getRequest().getMethod().matches("OPTIONS")) {
            String token = resolveToken(exchange.getRequest());
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                // System.out.println("Token : " + token);
                Authentication authentication = jwtUtil.getAuthentication(token);
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        }

        String first = exchange.getRequest().getHeaders().getFirst("Access-Control-Request-Headers");

        if (StringUtils.hasText(first) && first.contains("authorization")) {

            // System.out.println("Headers ==== " + exchange.getRequest().getHeaders().toString());
            // System.out.println();
            
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Access-Control-Allow-Headers", "*");
            response.getHeaders().add("Access-Control-Allow-Methods", "GET,PUT,POST,PATCH,DELETE,OPTIONS");
            response.getHeaders().add("Access-Control-Allow-Origin", "https://howzz-frontend.herokuapp.com");
            response.getHeaders().add("Access-Control-Max-Age", "-1");
            
            
            // System.out.println();
            // System.out.println("Response Header === " + exchange.getResponse().getHeaders().toString());
            // System.out.println();
            
                    
                    
                    
            // System.out.println("Pre-flight");
            return Mono.empty();
        }

        return chain.filter(exchange);
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        // String method = request.getHeaders().toString();
        // System.out.println(method);
        // System.out.println("Bearer token === " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
