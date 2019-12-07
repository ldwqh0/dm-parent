package com.dm.security.oauth2.access;

import java.util.List;

/**
 * 资源请求权限配置接口
 * 
 * @author ldwqh0@outlook.com
 * 
 * @since 0.2.1
 *
 */
public interface RequestAuthorityService {
    /**
     * 获取所有的资源权限配置
     * 
     * @return
     */
    public List<RequestAuthorityAttribute> listAllAuthorityAttribute();
}
