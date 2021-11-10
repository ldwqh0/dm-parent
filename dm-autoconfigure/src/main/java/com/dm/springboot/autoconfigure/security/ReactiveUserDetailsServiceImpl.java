package com.dm.springboot.autoconfigure.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        // TODO 这里待实现
        CompletableFuture.supplyAsync(() -> null);
        UserDetails ud = null;// userDetailsService.loadUserByUsername(username);
        return Mono.fromFuture(CompletableFuture.completedFuture(null));
    }

}
