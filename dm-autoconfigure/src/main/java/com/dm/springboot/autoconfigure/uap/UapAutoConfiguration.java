package com.dm.springboot.autoconfigure.uap;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dm.springboot.autoconfigure.uap.UapAutoConfiguration.UapJCacheConfiguration;
import com.dm.springboot.autoconfigure.uap.UapAutoConfiguration.UapBeanConfiguration;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.entity.Role.Status;
import com.dm.uap.entity.RoleGroup;
import com.dm.uap.service.RoleGroupService;
import com.dm.uap.service.RoleService;
import com.dm.uap.service.UserService;

@Configuration
@ConditionalOnClass(User.class)
@EntityScan({ "com.dm.uap" })
@EnableJpaRepositories({ "com.dm.uap" })
@ComponentScan({ "com.dm.uap" })
@EnableConfigurationProperties({ DefaultUserProperties.class })
@Import({ UapJCacheConfiguration.class, UapBeanConfiguration.class })
public class UapAutoConfiguration implements InitializingBean {

    @Autowired
    private DefaultUserProperties defaultUser;

    @Autowired
    private RoleService roleService;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private RoleGroupService roleGroupService;

    private void initRole() {
        Optional<RoleGroup> defaultGroupOptional = roleGroupService.findByName("内置分组");
        RoleGroup defaultGroup = defaultGroupOptional.get();
        RoleGroupDto drg = new RoleGroupDto();
        drg.setId(defaultGroup.getId());

        // 增加默认管理员角色
        if (!roleService.findByName("ROLE_ADMIN").isPresent()) {
            RoleDto role = new RoleDto();
            role.setName("ROLE_ADMIN");
            role.setGroup(drg);
            role.setDescription("系统内置管理员角色");
            role.setState(Status.ENABLED);
            roleService.save(role);
        }
        // 增加默认普通用户角色
        if (!roleService.findByName("ROLE_USER").isPresent()) {
            RoleDto role = new RoleDto();
            role.setName("ROLE_USER");
            role.setGroup(drg);
            role.setState(Status.ENABLED);
            role.setDescription("系统内置普通用户角色");
            roleService.save(role);
        }
        // 增加默认匿名用户角色
        if (!roleService.findByName("ROLE_ANONYMOUS").isPresent()) {
            RoleDto role = new RoleDto();
            role.setName("ROLE_ANONYMOUS");
            role.setGroup(drg);
            role.setDescription("系统内置匿名角色");
            role.setState(Status.ENABLED);
            roleService.save(role);
        }
    }

    private void initUser() {
        String username = defaultUser.getUsername();
        String password = defaultUser.getPassword();
        String fullname = defaultUser.getFullname();

        if (!userService.exist()) {
            UserDto user = new UserDto();
            user.setUsername(username);
            user.setFullname(fullname);
            user.setPassword(password);
            user.setEnabled(true);
            Optional<Role> _role = roleService.findByName("ROLE_ADMIN");
            if (_role.isPresent()) {
                RoleDto role = new RoleDto();
                role.setId(_role.get().getId());
                user.setRoles(Collections.singletonList(role));
            }
            userService.save(user);
        }
    }

    private void initRoleGroup() {
        if (!roleGroupService.exist()) {
            RoleGroupDto roleGroup = new RoleGroupDto();
            roleGroup.setName("内置分组");
            roleGroup.setDescription("系统默认角色分组");
            roleGroupService.save(roleGroup);
        }
    }

    /**
     * 初始化默认用户
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化角色组
        initRoleGroup();
        // 初始化角色
        initRole();
        // 初始化用户
        initUser();
    }

    static class UapBeanConfiguration {
        @Bean
        @ConditionalOnMissingBean(PasswordEncoder.class)
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @ConditionalOnClass(CacheManager.class)
    static class UapJCacheConfiguration {
        // 自动创建cache
        @Autowired(required = false)
        public void setCacheManager(CacheManager cacheManager) {
            MutableConfiguration<String, Object> configuration = new MutableConfiguration<String, Object>()
                    .setTypes(String.class, Object.class)
                    .setStoreByValue(false)
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_DAY));
            // 创建默认的用户cache
            Cache<String, Object> userCache = cacheManager.getCache("users");
            if (Objects.isNull(userCache)) {
                cacheManager.createCache("users", configuration);
            }
        }
    }
}
