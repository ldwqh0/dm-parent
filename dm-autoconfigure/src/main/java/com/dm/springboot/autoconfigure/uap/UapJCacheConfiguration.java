package com.dm.springboot.autoconfigure.uap;

import java.util.Objects;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

@ConditionalOnClass(CacheManager.class)
public class UapJCacheConfiguration implements InitializingBean {

    private CacheManager cacheManager;

    @Autowired(required = false)
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    };

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!Objects.isNull(cacheManager)) {
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
