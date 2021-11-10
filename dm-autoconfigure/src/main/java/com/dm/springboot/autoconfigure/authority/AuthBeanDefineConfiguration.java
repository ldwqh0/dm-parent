package com.dm.springboot.autoconfigure.authority;

import com.dm.auth.controller.*;
import com.dm.auth.repository.MenuRepository;
import com.dm.auth.repository.ResourceRepository;
import com.dm.auth.repository.RoleRepository;
import com.dm.auth.service.MenuService;
import com.dm.auth.service.ResourceService;
import com.dm.auth.service.RoleService;
import com.dm.auth.service.impl.MenuServiceImpl;
import com.dm.auth.service.impl.ResourceServiceImpl;
import com.dm.auth.service.impl.RoleServiceImpl;
import com.dm.springboot.autoconfigure.DmEntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

@DmEntityScan({"com.dm.auth"})
public class AuthBeanDefineConfiguration {

    @Bean
    public MenuRepository menuRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(MenuRepository.class);
    }

    @Bean
    public ResourceRepository resourceRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(ResourceRepository.class);
    }

    @Bean
    public RoleRepository roleRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(RoleRepository.class);
    }

    @Bean
    public RoleService roleService(RoleRepository roleRepository, MenuRepository menuRepository, EntityManager entityManager) {
        return new RoleServiceImpl(roleRepository, menuRepository, entityManager);
    }

    @Bean
    public ResourceServiceImpl resourceServiceImpl(ResourceRepository resourceRepository, RoleRepository roleRepository) {
        return new ResourceServiceImpl(resourceRepository, roleRepository);
    }

    @Bean
    public MenuService menuService(MenuRepository menuRepository, RoleRepository roleRepository) {
        return new MenuServiceImpl(menuRepository, roleRepository);
    }


    @Bean
    public ScopeController scopeController(ResourceService resourceService) {
        return new ScopeController(resourceService);
    }

    @Bean
    public RoleGroupController roleGroupController(RoleService roleService) {
        return new RoleGroupController(roleService);
    }

    @Bean
    public RoleController roleController(RoleService roleService) {
        return new RoleController(roleService);
    }

    @Bean
    public ResourceController resourceController(ResourceService resourceService) {
        return new ResourceController(resourceService);
    }

    @Bean
    public MenuController menuController(MenuService menuService) {
        return new MenuController(menuService);
    }

    @Bean
    public MenuAuthorityController menuAuthorityController(RoleService roleService) {
        return new MenuAuthorityController(roleService);
    }

}
