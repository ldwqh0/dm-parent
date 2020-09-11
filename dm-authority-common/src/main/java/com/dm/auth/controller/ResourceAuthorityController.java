package com.dm.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.converter.AuthorityConverter;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Resource;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.service.AuthorityService;
import com.dm.collections.Maps;
import com.dm.common.exception.DataNotExistException;

@RestController
@RequestMapping({ "resourceAuthorities", "p/resourceAuthorities" })
public class ResourceAuthorityController {

    private final AuthorityService authorityService;

    private final AuthorityConverter authorityConverter;

    @Autowired
    public ResourceAuthorityController(AuthorityService authorityService, AuthorityConverter authorityConverter) {
        this.authorityService = authorityService;
        this.authorityConverter = authorityConverter;
    }

    /**
     * 保存一组资源授权设置
     * 
     * @param resourceAuthority
     * @return
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
     * @param roleId
     * @return
     */
    // 增加事务的目的是为了在序列化的过程中，某些延迟加载的对象能找到连接
    // 否则会抛出 org.hibernate.LazyInitializationException 异常
    @Transactional(readOnly = true)
    @GetMapping("{rolename}")
    public ResourceAuthorityDto get(@PathVariable("rolename") String rolename) {
        return authorityService.findByRoleName(rolename)
                .map(this::toResourceAuthorityDto)
                .orElseThrow(DataNotExistException::new);
    }

    private ResourceAuthorityDto toResourceAuthorityDto(Authority authority) {
        ResourceAuthorityDto dto = new ResourceAuthorityDto();
        dto.setRoleId(authority.getId());
        dto.setRoleName(authority.getRoleName());
        Map<Long, ResourceOperation> result = Maps.transformKeys(authority.getResourceOperations(), Resource::getId);
        dto.setResourceAuthorities(result);
        return dto;
    }

    /**
     * 删除指定角色的资源授权设置
     * 
     * @param roleId
     */
    @DeleteMapping("{rolename}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByRoleId(@PathVariable("rolename") String rolename) {
        authorityService.deleteResourceAuthoritiesByRoleName(rolename);
    }

}
