package com.dm.security.oauth2.server.resource.introspection;

import com.dm.security.oauth2.core.IntrospectorClaimsSetConverter;
import com.dm.security.oauth2.core.UserDetailsDtoClaimsSetConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class UserInfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private final String url;
    private final String clientId;
    private final String clientSecret;

    private IntrospectorClaimsSetConverter claimsSetConverter = new UserDetailsDtoClaimsSetConverter();

    private RestOperations restOperations;

    public void setClaimsSetConverter(IntrospectorClaimsSetConverter claimsSetConverter) {
        this.claimsSetConverter = claimsSetConverter;
    }

    public UserInfoOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        this.url = introspectionUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        RestTemplate restTemplate = new RestTemplate();
        new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(clientId, clientSecret));
        this.restOperations = restTemplate;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        Map<String, Object> map = this.loadUserInfo(token);
        return this.claimsSetConverter.convert(map);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> loadUserInfo(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        return restOperations.exchange(request, Map.class).getBody();
    }

}
