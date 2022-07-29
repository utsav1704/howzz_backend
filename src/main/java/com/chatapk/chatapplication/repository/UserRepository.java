package com.chatapk.chatapplication.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.chatapk.chatapplication.models.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User,String>{
    
    @Query("{email:?0}")
    public Mono<User> loadUserByEmail(String email);

    @Query("{userId:{$regex : ?0}}")
    public Flux<User> findByUserId(String userId);
}
