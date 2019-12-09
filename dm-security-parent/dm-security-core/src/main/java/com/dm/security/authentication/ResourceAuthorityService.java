package com.dm.security.authentication;

import java.util.List;

/**
 * 资源请求权限配置接口
 * 
 * @author ldwqh0@outlook.com
 * 
 * @since 0.2.1
 *
 */
public interface ResourceAuthorityService {
    /**
     * 获取所有的资源权限配置
     * 
     * @return
     */
    public List<ResourceAuthorityAttribute> listAll();
}
