package com.chatapk.chatapplication.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
        .csrf()
        .disable()
                .authorizeExchange()
                // .pathMatchers("/user/auth/**")
                .pathMatchers("/user/email/**","/user/auth/**")
                .hasRole("USER")
                .pathMatchers("/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/user/auth/success"))
                .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler("/user/auth/fail"))
                .and()
                .logout()
                .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"));
        
                httpSecurity.cors();

        return httpSecurity.build();
    }

    @Bean
    public ReactiveUserDetailsService userDetails() {
        return new CustomeUserDetails();
    }

}
