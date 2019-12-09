package com.dm.security.authentication;

import java.io.Serializable;
import java.util.Set;

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

    public String getName();

    public String getPath();
    
    public MatchType getMatchType();
    
    public Set<String> getScope();

}
