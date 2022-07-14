package com.chatapk.chatapplication.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.chatapk.chatapplication.handler.UserHandler;
// @CrossOrigin

@Configuration
public class RouterConfig {

    @Autowired
    private UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(){
        return RouterFunctions.route()
            .POST("/user", userHandler::addUser)
            .GET("/user/auth/{msg}",userHandler::loadUserDashboard)
            .GET("/user/email/getInfo",userHandler::getLoggedUser)
            .GET("/user/{id}",userHandler::getUser)
            .GET("/user/email/{email}",userHandler::getUserByEmail)
            .GET("/login",userHandler::loadLoginPage)
            .GET("/",userHandler::loadLoginPage)
            .GET("/signup",userHandler::loadRegisterPage)
            .POST("/signup/uploadImage/{id}", userHandler::uploadImage)
            .build();
    }
}
