package com.dm.security.oauth2.server.resource.introspection;

import java.net.URI;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class UserInfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private String url = "http://localhost:8887/oauth/users/current";
    private RestTemplate restTemplate;

    private PrincipalExtractor principalExtractor = new UserDetailsDtoPrincipalExtractor();

    public UserInfoOpaqueTokenIntrospector(String introspectionUri) {
        this.url = introspectionUri;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        Map<String, Object> map = this.loadUserInfo(token);
        return principalExtractor.extract(map);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> loadUserInfo(String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + token);
        RequestEntity<Null> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> re = restTemplate.exchange(request, Map.class);
        return re.getBody();
    }

}
