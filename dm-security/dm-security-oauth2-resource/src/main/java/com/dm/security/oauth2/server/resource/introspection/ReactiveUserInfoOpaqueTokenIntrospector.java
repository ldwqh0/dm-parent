package com.dm.security.oauth2.server.resource.introspection;

import com.dm.security.oauth2.core.PrincipalExtractor;
import com.dm.security.oauth2.core.UserDetailsDtoPrincipalExtractor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ReactiveUserInfoOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {
    // TODO 这里需要处理
    private final String url;
    private final WebClient webClient;

    private final PrincipalExtractor principalExtractor = new UserDetailsDtoPrincipalExtractor();

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
