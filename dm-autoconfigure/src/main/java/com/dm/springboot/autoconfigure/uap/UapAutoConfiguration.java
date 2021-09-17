package com.dm.springboot.autoconfigure.uap;

import com.dm.springboot.autoconfigure.uap.UapAutoConfiguration.UapJCacheConfiguration;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserRoleDto;
import com.dm.uap.entity.User;
import com.dm.uap.service.UserService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.Collections;
import java.util.Objects;

@ConditionalOnClass(User.class)
@EntityScan({"com.dm.uap"})
@EnableJpaRepositories({"com.dm.uap"})
@ComponentScan({"com.dm.uap"})
@EnableConfigurationProperties({DefaultUserProperties.class})
@Import({UapJCacheConfiguration.class, UapAutoConfiguration.UapBeanConfiguration.class})
public class UapAutoConfiguration implements InitializingBean {

    private final DefaultUserProperties defaultUser;

    private final UserService userService;

    public UapAutoConfiguration(DefaultUserProperties defaultUser, UserService userService) {
        this.defaultUser = defaultUser;
        this.userService = userService;
    }

    private void initUser() {
        String username = defaultUser.getUsername();
        String password = defaultUser.getPassword();
        String fullname = defaultUser.getFullname();

        // 建立初始管理员账号
        if (!userService.exist()) {
            UserDto user = new UserDto();
            user.setUsername(username);
            user.setFullname(fullname);
            user.setPassword(password);
            user.setEnabled(true);
            user.setRoles(Collections.singleton(UserRoleDto.ROLE_ADMIN));
            userService.save(user);
        }
        // 建立默认匿名账号
        if (!userService.userExistsByUsername("ANONYMOUS")) {
            UserDto anonymous = new UserDto();
            anonymous.setUsername("ANONYMOUS");
            anonymous.setPassword("ANONYMOUS");
            anonymous.setEnabled(true);
            anonymous.setFullname("匿名用户");
            anonymous.setRoles(Collections.singleton(UserRoleDto.ROLE_ANONYMOUS));
            userService.save(anonymous);
        }
    }

    /**
     * 初始化默认用户
     */
    @Override
    public void afterPropertiesSet() {
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
                .setTypes(String.class, Object.class).setStoreByValue(false)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.TEN_MINUTES));
            // 创建默认的用户cache
            Cache<String, Object> userCache = cacheManager.getCache("users");
            if (Objects.isNull(userCache)) {
                cacheManager.createCache("users", configuration);
            }
        }
    }
}
