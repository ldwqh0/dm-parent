package com.dm.springboot.autoconfigure.jdbc;

import com.dm.datasource.mulit.AutoCreateRoutingDataSource;
import com.dm.datasource.mulit.DetermineCurrentLookupKeyStrategy;
import com.dm.datasource.mulit.ThreadLocalDetermineCurrentLookupKeyStrategy;
import com.dm.datasource.provider.DataSourceProvider;
import com.dm.datasource.provider.DataSourceProviderHolder;
import com.dm.datasource.provider.MySQL8DataSourceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnBean(AutoCreateRoutingDataSource.class)
public class MultiDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DetermineCurrentLookupKeyStrategy.class)
    public DetermineCurrentLookupKeyStrategy determineCurrentLookupKeyStrategy() {
        return new ThreadLocalDetermineCurrentLookupKeyStrategy();
    }

    @Bean
    public MySQL8DataSourceProvider mySQL8DataSourceProvider() {
        return new MySQL8DataSourceProvider();
    }

    @Configuration
    static class DataSourceProviderConfiguration {
        DataSourceProviderConfiguration(List<DataSourceProvider> providers) {
            providers.forEach(DataSourceProviderHolder::registerProvider);
        }
    }
}
