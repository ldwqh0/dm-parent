package com.dm.springboot.autoconfigure.uap;

import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserRoleDto;
import com.dm.uap.entity.User;
import com.dm.uap.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.Collections;
import java.util.Objects;

@ConditionalOnClass(User.class)
@EnableConfigurationProperties({DefaultUserProperties.class})
@Import({UapBeanDefineConfiguration.class, UapAutoConfiguration.UapJCacheConfiguration.class})
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
        String fullName = defaultUser.getFullName();

        // 建立初始管理员账号
        if (!userService.exist()) {
//            UserDto(String username, String fullName, String password, Set<UserRoleDto> roles)
            UserDto admin = UserDto.builder()
                .username(username)
                .fullName(fullName)
                .password(password)
                .roles(Collections.singleton(UserRoleDto.ROLE_ADMIN))
                .build();

            userService.save(admin);
        }
        // 建立默认匿名账号
        if (!userService.userExistsByUsername("ANONYMOUS")) {
            UserDto anonymous = UserDto.builder()
                .username("ANONYMOUS")
                .fullName("匿名用户")
                .password("N/A")
                .roles(Collections.singleton(UserRoleDto.ROLE_ANONYMOUS))
                .build();
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
