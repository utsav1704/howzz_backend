package com.chatapk.chatapplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import com.chatapk.chatapplication.models.User;
import com.chatapk.chatapplication.repository.UserRepository;

import reactor.core.publisher.Mono;

public class CustomeUserDetails implements ReactiveUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Mono<User> uMono = userRepository.loadUserByEmail(username);
        Mono<UserDetails> userDetails = uMono.map(user -> new UserDetailsImpl(user));
        return userDetails;
    }
}
