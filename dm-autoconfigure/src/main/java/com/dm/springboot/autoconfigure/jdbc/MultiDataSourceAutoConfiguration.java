package com.dm.springboot.autoconfigure.jdbc;

import com.dm.datasource.mulit.AutoCreateRoutingDataSource;
import com.dm.datasource.mulit.DetermineCurrentLookupKeyStrategy;
import com.dm.datasource.mulit.ThreadLocalDetermineCurrentLookupKeyStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(AutoCreateRoutingDataSource.class)
public class MultiDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DetermineCurrentLookupKeyStrategy.class)
    public DetermineCurrentLookupKeyStrategy determineCurrentLookupKeyStrategy() {
        return new ThreadLocalDetermineCurrentLookupKeyStrategy();
    }

}
