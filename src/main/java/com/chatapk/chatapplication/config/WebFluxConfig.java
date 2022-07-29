package com.chatapk.chatapplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;


@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/user/email/**")
            .allowCredentials(false)
            .allowedMethods( "PUT" , "GET" , "POST", "PATCH", "DELETE","OPTIONS")
            .allowedOrigins("https://howzz-frontend.herokuapp.com")
            .allowedHeaders("Authentication")
            .maxAge(-1);

        registry.addMapping("/**")
        .allowCredentials(true)
        .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE","OPTIONS")
            .allowedOrigins("https://howzz-frontend.herokuapp.com")
            // .allowedOriginPatterns()
            // .allowedHeaders("*")
            .allowedHeaders("*")
            // .exposedHeaders("Origin", "Content-Type", "Accept", "Authorization",
            // "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
            .maxAge(-1);

        
    }
}
