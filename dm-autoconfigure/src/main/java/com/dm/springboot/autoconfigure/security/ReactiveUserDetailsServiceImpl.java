package com.dm.springboot.autoconfigure.security;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import reactor.core.publisher.Mono;

public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        CompletableFuture.supplyAsync(() -> {
            return null;
        });

        UserDetails ud = null;// userDetailsService.loadUserByUsername(username);
        return Mono.fromFuture(CompletableFuture.completedFuture(ud));
    }

}
