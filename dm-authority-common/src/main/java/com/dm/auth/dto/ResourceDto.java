package com.dm.auth.dto;

import com.dm.security.authentication.UriResource.MatchType;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class ResourceDto implements Serializable {
    private static final long serialVersionUID = -847613336378865468L;
    /**
     * 资源id
     */
    private Long id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源匹配字符串
     */
    private String matcher;
    /**
     * 资源描述
     */
    private String description;
    /**
     * 资源Scope
     */
    private Set<String> scope;
    /**
     * 资源匹配方式
     */
    private MatchType matchType;
}
