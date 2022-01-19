package com.dm.security.authentication;

import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * 标识个Uri资源，用来标识一个唯一的URI资源
 *
 * @author ldwqh0@outlook.com
 */
public interface UriResource extends Serializable {

    /**
     * 资源匹配模式
     *
     * @author ldwqh0@outlook.com
     */
    enum MatchType {
        /**
         * 路径匹配
         */
        ANT_PATH,
        /**
         * 正则表达式匹配
         */
        REGEXP,
    }

    Optional<HttpMethod> getMethod();

    String getUri();

    MatchType getMatchType();

    Set<String> getScopes();

    static UriResource of(HttpMethod method, String path, MatchType matchType, Set<String> scopes) {
        return new UriResourceImpl(method, path, matchType, scopes);
    }

    static UriResource of(String path, MatchType matchType, Set<String> scopes) {
        return new UriResourceImpl(null, path, matchType, scopes);
    }

    static UriResource of(String path) {
        return new UriResourceImpl(null, path, MatchType.ANT_PATH, Collections.emptySet());
    }

    static UriResource of(String path, MatchType matchType) {
        return new UriResourceImpl(null, path, matchType, Collections.emptySet());
    }

    static UriResource of(HttpMethod method) {
        return new UriResourceImpl(method, "/**", MatchType.ANT_PATH, Collections.emptySet());
    }

    static UriResource of(HttpMethod method, Set<String> scopes) {
        return new UriResourceImpl(method, "/**", MatchType.ANT_PATH, scopes);
    }

}

class UriResourceImpl implements UriResource {

    private static final long serialVersionUID = 9029779605843604190L;
    private final String uri;
    private final MatchType type;
    private final HttpMethod method;
    private final Set<String> scopes;

    public UriResourceImpl(HttpMethod method, String path, MatchType type, Set<String> scopes) {
        super();
        this.method = method;
        this.uri = path;
        this.type = type;
        this.scopes = scopes;
    }

    @Override
    public Optional<HttpMethod> getMethod() {
        return Optional.ofNullable(method);
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public Set<String> getScopes() {
        return scopes;
    }

    @Override
    public MatchType getMatchType() {
        return type;
    }
}
