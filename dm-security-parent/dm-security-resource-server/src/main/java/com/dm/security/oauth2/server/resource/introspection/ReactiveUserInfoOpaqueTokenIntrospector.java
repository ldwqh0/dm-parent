package com.dm.security.oauth2.server.resource.introspection;

import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class ReactiveUserInfoOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {
    private String url = "http://localhost:8887/oauth/users/current";
    private WebClient webClient;

    private PrincipalExtractor principalExtractor = new UserDetailsDtoPrincipalExtractor();

    public ReactiveUserInfoOpaqueTokenIntrospector(String introspectionUri) {
        this.url = introspectionUri;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        return this.webClient.get().uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve().bodyToMono(Map.class)
                .map(principalExtractor::extract);
    }

}
