package com.dm.auth.controller;

import com.dm.auth.converter.AuthorityConverter;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.AuthResource;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.service.AuthorityService;
import com.dm.collections.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"resourceAuthorities", "p/resourceAuthorities"})
public class ResourceAuthorityController {

    private final AuthorityService authorityService;

    private final AuthorityConverter authorityConverter;

    public ResourceAuthorityController(AuthorityService authorityService, AuthorityConverter authorityConverter) {
        this.authorityService = authorityService;
        this.authorityConverter = authorityConverter;
    }

    /**
     * 保存一组资源授权设置
     *
     * @param resourceAuthority 资源授权配置
     * @return 保存后的资源授权配置
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResourceAuthorityDto save(@RequestBody ResourceAuthorityDto resourceAuthority) {
        Authority authority = authorityService.save(resourceAuthority);
        return authorityConverter.toResourceAuthorityDto(authority);
    }

    /**
     * 获取指定角色的资源授权设置
     *
     * @param roleName 角色名称
     * @return 角色的资源授权信息
     */
    // 增加事务的目的是为了在序列化的过程中，某些延迟加载的对象能找到连接
    // 否则会抛出 org.hibernate.LazyInitializationException 异常
    @Transactional(readOnly = true)
    @GetMapping("{roleName}")
    public ResourceAuthorityDto get(@PathVariable("roleName") String roleName) {
        return authorityService.findByRoleName(roleName)
            .map(this::toResourceAuthorityDto)
            .orElse(null);
    }

    private ResourceAuthorityDto toResourceAuthorityDto(Authority authority) {
        ResourceAuthorityDto dto = new ResourceAuthorityDto();
        dto.setRoleId(authority.getId());
        dto.setRoleName(authority.getRoleName());
        Map<Long, ResourceOperation> result = Maps.transformKeys(authority.getResourceOperations(), AuthResource::getId);
        dto.setResourceAuthorities(result);
        return dto;
    }

    /**
     * 删除指定角色的资源授权设置
     *
     * @param rolename 角色名称
     */
    @DeleteMapping("{rolename}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByRoleId(@PathVariable("rolename") String rolename) {
        authorityService.deleteResourceAuthoritiesByRoleName(rolename);
    }

}
