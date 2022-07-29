package com.chatapk.chatapplication.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.chatapk.chatapplication.models.AuthenticationRequest;
import com.chatapk.chatapplication.models.User;
import com.chatapk.chatapplication.repository.UserRepository;
import com.chatapk.chatapplication.services.JwtUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserHandler {

    @Value("${baseUrl}")
    private String baseUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    public Mono<ServerResponse> addUser(ServerRequest serverRequest) {
        Mono<User> mono = serverRequest.bodyToMono(User.class);
        mono = mono.map(user -> {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return user;
    });
        Mono<User> flatMap = mono.flatMap(user -> userRepository.insert(user));
        // flatMap.subscribe(System.out::println);
        return ServerResponse.ok().body(flatMap, User.class);
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
        return ServerResponse.permanentRedirect(URI.create(baseUrl)).build();
    }

    public Mono<ServerResponse> loadRegisterPage(ServerRequest serverRequest) {
        return ServerResponse.permanentRedirect(URI.create(baseUrl + "/signup")).build();
    }

    public Mono<ServerResponse> loadUserDashboard(ServerRequest serverRequest) {
        String msg = serverRequest.pathVariable("msg").toString();

        if (msg.equals("success")) {
            // Mono<? extends Principal> principal = serverRequest.principal();

            // return ServerResponse.ok().body(user , User.class);
            return ServerResponse.permanentRedirect(URI.create(baseUrl + "/dashboard")).build();
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
        return ServerResponse.ok().body(user, User.class);
    }

    public Mono<ServerResponse> doLogin(ServerRequest serverRequest){
        System.out.println("Here");
        Mono<AuthenticationRequest> authRequest = serverRequest.bodyToMono(AuthenticationRequest.class);
        Mono<String> map = authRequest
            .flatMap(request -> authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()))
            .map(jwtUtil::createToken)
            )
            .map(jwt -> {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer "+jwt);
                // var body = Map.of("access_token",jwt);
                // return ServerResponse.ok().body(body , Map.class);
                return jwt;
            });
        return ServerResponse.ok().body(map , String.class);
    }

    public Mono<ServerResponse> handlePreflightRequest(ServerRequest serverRequest){
        System.out.println("Pre-flight");
        //   response.setHeader("Access-Control-Allow-Origin", "*");
        //   response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT");
        //   response.setHeader("Access-Control-Max-Age", "3600");
        //   response.setHeader("Access-Control-Allow-Headers", "Access-Control-Expose-Headers"+"Authorization, content-type," +
        //   "USERID"+"ROLE"+
        //           "access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with,responseType,observe");
        return ServerResponse.ok()
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT")
            .header("Access-Control-Max-Age", "3600")
            .build();
            
    }

    public Mono<ServerResponse> findUsers(ServerRequest serverRequest){
        String userId = serverRequest.pathVariable("userId");
        Flux<User> users = userRepository.findByUserId(userId);
        return ServerResponse.ok().body(users,User.class);
    }

}
