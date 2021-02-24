package com.dm.security.oauth2.server.resource.introspection;

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

/**
 * token内省，可以将内省结果转换为指定的{@link OAuth2AuthenticatedPrincipal}结构 ,但需要我们配置自己的转换器
 * <p>在资源服务器中使用</p>
 */
public class UserInfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private final String url;

    private IntrospectorClaimsSetConverter claimsSetConverter = new UserDetailsDtoClaimsSetConverter();

    private final RestOperations restOperations;

    public void setClaimsSetConverter(IntrospectorClaimsSetConverter claimsSetConverter) {
        this.claimsSetConverter = claimsSetConverter;
    }

    public UserInfoOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        this.url = introspectionUri;
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
