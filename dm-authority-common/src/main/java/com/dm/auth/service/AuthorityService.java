package com.dm.auth.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.client.HttpServerErrorException;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.security.provider.RequestAuthoritiesService;

public interface AuthorityService extends RequestAuthoritiesService {

    public Authority save(MenuAuthorityDto authorityDto);

    public Optional<Authority> get(String rolename);

    public boolean exists();

    /**
     * 保存资源授权信息
     * 
     * @param resourceAuthority
     * @return
     */
    public Authority save(ResourceAuthorityDto resourceAuthority);

    /**
     * 根据角色，删除资源权限配置
     * 
     * @param roleId
     */
    public void deleteResourceAuthoritiesByRoleName(String rolenamme);

    public List<Menu> listMenuByAuthorities(List<String> authorities);

    /**
     * 判断指定的角色是否有权限访问指定的资源
     * 
     * @param authority 要验证的角色
     * 
     * @param request   要访问的资源
     * @return
     */
    public boolean access(Object authority, HttpServletRequest request);

}
