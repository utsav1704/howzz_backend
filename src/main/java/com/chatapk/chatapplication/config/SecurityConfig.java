package com.chatapk.chatapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.chatapk.chatapplication.services.JwtUtil;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
	// public WebMvcConfigurer corsConfigurer() {
	// 	return new WebMvcConfigurer() {
	// 		@Override
	// 		public void addCorsMappings(CorsRegistry registry) {
	// 			registry.addMapping("/**").allowedOrigins("http://localhost:3000")
    //             .allowedMethods("GET","PUT", "POST", "PATCH", "DELETE","OPTIONS")
    //             .allowedHeaders("*")
    //             .maxAge(-1);
	// 		}
	// 	};
	// }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity , JwtUtil jwtUtil , ReactiveAuthenticationManager reactiveAuthenticationManager) {
        httpSecurity
        // .cors()
        // .configurationSource(corsConfigurationSource())
        // .and()
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authenticationManager(reactiveAuthenticationManager)
        .authorizeExchange(authExchange -> authExchange
            .pathMatchers("/signup","/signin").permitAll()
            .pathMatchers("/user/email/**","/user/auth/**").hasRole("USER")
            .anyExchange().permitAll()
            )
                // .authorizeExchange()
                // // .pathMatchers(HttpMethod.OPTIONS, "/**")
                // // .permitAll()
                // // .pathMatchers("/user/auth/**")
                // .pathMatchers("/user/email/**","/user/auth/**")
                // .hasRole("USER")
                // // 
                // .anyExchange()
                // .permitAll()
                // .and()
                // .formLogin()
                // // .loginPage("/login")
                // // .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/user/auth/success"))
                // // .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler("/user/auth/fail"))
                // .and()
                // .logout()
                // .logoutHandler(new SecurityContextServerLogoutHandler())
                // .and()
                .addFilterAt(new JwtTokenAuthenticationFilter(jwtUtil), SecurityWebFiltersOrder.HTTP_BASIC).cors();
        
                httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);


        return httpSecurity.build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationManager;
    }

    // @Bean
    // public FilterRegistrationBean corsConfigurationSource(){
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowCredentials(true);
    //     configuration.addAllowedOrigin("http://localhost:3000");
    //     configuration.addAllowedHeader("*");
    //     configuration.addAllowedMethod("GET");
    //     configuration.addAllowedMethod("POST");
    //     configuration.addAllowedMethod("PUT");
    //     configuration.addAllowedMethod("DELETE");
    //     configuration.addAllowedMethod("PATCH");
    //     configuration.addAllowedMethod("OPTIONS");
    //     configuration.setMaxAge(-1L);

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);

    //     FilterRegistrationBean filter = new
        
    //     return null;
    //     // return source;
    // }

    @Bean
    public ReactiveUserDetailsService userDetails() {
        return new CustomeUserDetails();
    }

}
