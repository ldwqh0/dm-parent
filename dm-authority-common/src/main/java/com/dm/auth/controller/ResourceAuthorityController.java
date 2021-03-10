package com.dm.auth.controller;

import com.dm.auth.converter.RoleConverter;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.AuthResource;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.entity.Role;
import com.dm.auth.service.RoleService;
import com.dm.collections.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 资源授权配置
 */
@RestController
@RequestMapping({"resourceAuthorities", "p/resourceAuthorities"})
@RequiredArgsConstructor
public class ResourceAuthorityController {

    private final RoleService authorityService;

    private final RoleConverter roleConverter;


    /**
     * 保存一组资源授权设置
     *
     * @param resourceAuthority 资源授权配置
     * @return 保存后的资源授权配置
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResourceAuthorityDto save(@RequestBody ResourceAuthorityDto resourceAuthority) {
        Role authority = authorityService.saveAuthority(resourceAuthority);
        return roleConverter.toResourceAuthorityDto(authority);
    }

    /**
     * 获取指定角色的资源授权设置
     *
     * @param roleId 角色Id
     * @return 角色的资源授权信息
     */
    // 增加事务的目的是为了在序列化的过程中，某些延迟加载的对象能找到连接
    // 否则会抛出 org.hibernate.LazyInitializationException 异常
    @Transactional(readOnly = true)
    @GetMapping("{roleId}")
    public ResourceAuthorityDto get(@PathVariable("roleId") Long roleId) {
        return authorityService.findById(roleId).map(this::toResourceAuthorityDto).orElse(null);
    }


    /**
     * 删除指定角色的资源授权设置
     *
     * @param roleId 角色名称
     * @ignore 暂时不输出该接口
     */
    @DeleteMapping("{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByRoleId(@PathVariable("roleId") Long roleId) {
//        authorityService.deleteResourceAuthoritiesByRoleName(roleId);
    }

    private ResourceAuthorityDto toResourceAuthorityDto(Role authority) {
        ResourceAuthorityDto dto = new ResourceAuthorityDto();
        dto.setRoleId(authority.getId());
        dto.setRoleName(authority.getName());
        Map<Long, ResourceOperation> result = Maps.transformKeys(authority.getResourceOperations(),
            AuthResource::getId);
        dto.setResourceAuthorities(result);
        return dto;
    }
}
