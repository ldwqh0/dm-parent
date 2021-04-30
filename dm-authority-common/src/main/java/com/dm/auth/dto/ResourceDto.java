package com.dm.auth.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.security.authentication.UriResource.MatchType;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class ResourceDto implements Serializable, IdentifiableDto<Long> {
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
     * 该资源的方法
     */
    private Set<HttpMethod> methods = new HashSet<>();

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

    /**
     * 允许访问角色信息
     */
    private Set<RoleDto> accessAuthorities = new HashSet<>();

    /**
     * 拒绝访问角色信息
     *
     * @return
     */
    private Set<RoleDto> denyAuthorities = new HashSet<>();

    /**
     * 指示是否拒绝所有的访问
     */
    private boolean denyAll = true;

    /**
     * 是否必须授权访问
     */
    private boolean authenticated;
}
