package com.chatapk.chatapplication.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.chatapk.chatapplication.models.User;
import com.chatapk.chatapplication.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Mono<ServerResponse> addUser(ServerRequest serverRequest) {
        Mono<User> mono = serverRequest.bodyToMono(User.class);
        mono = mono.map(user -> extracted(user));
        Mono<User> flatMap = mono.flatMap(user -> userRepository.insert(user));
        flatMap.subscribe(System.out::println);
        return ServerResponse.ok().body(flatMap, User.class);
    }

    private User extracted(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return user;
    }

    public Mono<ServerResponse> uploadImage(ServerRequest serverRequest) {
        // System.out.println("Here................");
        // String id = serverRequest.pathVariable("id").toString();
        // imageKitService.initializeImageKit();
        // Mono<ImageKit> imageKit = Mono.just(imageKitService.getImageKit());
        // Mono<Map<String, String>> map = imageKit.map(img ->
        // img.getAuthenticationParameters());
        return ServerResponse.ok().build();
        // return ServerResponse.ok().build();
    }

    public Mono<ServerResponse> getUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<User> user = userRepository.findById(id);
        return ServerResponse.ok().body(user, User.class);
    }

    public Mono<ServerResponse> getUserByEmail(ServerRequest serverRequest) {
        String email = serverRequest.pathVariable("email");
        Mono<User> user = userRepository.loadUserByEmail(email);
        return ServerResponse.ok().body(user, User.class);
    }

    public Mono<ServerResponse> loadLoginPage(ServerRequest serverRequest) {
        return ServerResponse.permanentRedirect(URI.create("https://howzz-frontend.herokuapp.com")).build();
    }

    public Mono<ServerResponse> loadRegisterPage(ServerRequest serverRequest) {
        return ServerResponse.permanentRedirect(URI.create("https://howzz-frontend.herokuapp.com/signup")).build();
    }

    public Mono<ServerResponse> loadUserDashboard(ServerRequest serverRequest) {
        String msg = serverRequest.pathVariable("msg").toString();

        if (msg.equals("success")) {
            // Mono<? extends Principal> principal = serverRequest.principal();

            // return ServerResponse.ok().body(user , User.class);
            return ServerResponse.permanentRedirect(URI.create("https://howzz-frontend.herokuapp.com/dashboard")).build();
        }
        return ServerResponse.badRequest().build();
        // return
        // ServerResponse.permanentRedirect(URI.create("http://localhost:3000")).build();
        // Mono<? extends Principal> principal = serverRequest.principal();
        // System.out.println(principal);
        // Mono<String> map = principal.map(princ -> princ.getName());
        // Mono<User> activeUser = map.flatMap(user ->
        // userRepository.loadUserByEmail(user));
        // return ServerResponse.ok().body(activeUser , User.class);
        // return ServerResponse.ok().build();
    }

    public Mono<ServerResponse> getLoggedUser(ServerRequest serverRequest) {
        Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();
        Mono<Authentication> authMono = context.map(con -> con.getAuthentication());
        authMono.subscribe(System.out::println);
        Mono<User> user = authMono.flatMap(auth -> userRepository.loadUserByEmail(auth.getName()));
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(user, User.class);
    }
}
