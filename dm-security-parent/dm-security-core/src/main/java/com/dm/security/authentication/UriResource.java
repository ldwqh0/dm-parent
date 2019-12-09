package com.dm.security.authentication;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

/**
 * 标识个Uri资源，用来标识一个唯一的URI资源
 * 
 * @author ldwqh0@outlook.com
 *
 */
public interface UriResource extends Serializable {

    /**
     * 资源匹配模式
     * 
     * @author ldwqh0@outlook.com
     *
     */
    public enum MatchType {
        /**
         * 路径匹配
         */
        ANT_PATH,
        /**
         * 正则表达式匹配
         */
        REGEXP,
    }

    public String getMethod();

    public String getPath();

    public MatchType getMatchType();

    public Set<String> getScopes();

    public static UriResource of(String method, String path, MatchType matchType, Set<String> scopes) {
        return new UriResourceImpl(method, path, matchType, scopes);
    }

}

class UriResourceImpl implements UriResource {

    private static final long serialVersionUID = 9029779605843604190L;

    private String method;
    private String path;
    private MatchType type;
    private Set<String> scopes;

    public UriResourceImpl() {
        super();
    }

    public UriResourceImpl(String method, String path, MatchType type, Set<String> scopes) {
        super();
        this.method = method;
        this.path = path;
        this.type = type;
        this.scopes = scopes;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Set<String> getScopes() {
        return scopes;
    }

    @Override
    public MatchType getMatchType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((scopes == null) ? 0 : scopes.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UriResourceImpl other = (UriResourceImpl) obj;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (scopes == null) {
            if (other.scopes != null)
                return false;
        } else if (!CollectionUtils.isEqualCollection(scopes, other.scopes))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
