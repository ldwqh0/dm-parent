package com.dm.springboot.autoconfigure.authority;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.Objects;

@ConditionalOnClass(CacheManager.class)
@Configuration
public class AuthJCacheConfiguration implements InitializingBean {

    private CacheManager jCacheManager = null;

    @Autowired(required = false)
    public void setCacheManager(CacheManager cacheManager) {
        this.jCacheManager = cacheManager;
    }

    @Override
    public void afterPropertiesSet() {
        if (!Objects.isNull(jCacheManager)) {
            MutableConfiguration<String, Object> configuration = new MutableConfiguration<String, Object>()
                .setTypes(String.class, Object.class)
                .setStoreByValue(false)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_DAY));
            // 设置默认的AuthorityAttributes配置
            Cache<String, Object> cache = jCacheManager.getCache("AuthorityAttributes");
            Cache<String, Object> menuCache = jCacheManager.getCache("AuthorityMenus");
            if (Objects.isNull(cache)) {
                jCacheManager.createCache("AuthorityAttributes", configuration);
            }
            if (Objects.isNull(menuCache)) {
                jCacheManager.createCache("AuthorityMenus", configuration);
            }
        }
    }
}
