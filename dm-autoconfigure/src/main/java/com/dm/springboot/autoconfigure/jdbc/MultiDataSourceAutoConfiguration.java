package com.dm.springboot.autoconfigure.jdbc;

import com.dm.datasource.DataSourceBuilder;
import com.dm.datasource.HikariDataSourceBuilder;
import com.dm.datasource.mulit.AutoCreateRoutingDataSource;
import com.dm.datasource.mulit.DetermineCurrentLookupKeyStrategy;
import com.dm.datasource.mulit.ThreadLocalDetermineCurrentLookupKeyStrategy;
import com.dm.datasource.provider.DataSourceProvider;
import com.dm.datasource.provider.DataSourceProviderHolder;
import com.dm.datasource.provider.support.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConditionalOnBean(AutoCreateRoutingDataSource.class)
public class MultiDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DetermineCurrentLookupKeyStrategy.class)
    public DetermineCurrentLookupKeyStrategy determineCurrentLookupKeyStrategy() {
        return new ThreadLocalDetermineCurrentLookupKeyStrategy();
    }

    @Bean
    @ConditionalOnClass(name = "com.microsoft.sqlserver.jdbc.SQLServerDriver")
    public SqlServerDataSourceProvider sqlServerDataSourceProvider() {
        return new SqlServerDataSourceProvider();
    }

    @Bean
    @ConditionalOnClass(name = "com.mysql.cj.jdbc.Driver")
    public MySQL8DataSourceProvider mySQL8DataSourceProvider() {
        return new MySQL8DataSourceProvider();
    }

    @Bean
    @ConditionalOnClass(name = "oracle.jdbc.driver.OracleDriver")
    public OracleDataSourceProvider oracleDataSourceProvider() {
        return new OracleDataSourceProvider();
    }

    @Bean
    @ConditionalOnClass(name = "org.h2.Driver")
    public H2DataSourceProvider h2DataSourceProvider() {
        return new H2DataSourceProvider();
    }

    @Bean
    @ConditionalOnClass(name = "org.postgresql.Driver")
    public PostgreSQLDataSourceProvider postgreSQLDataSourceProvider() {
        return new PostgreSQLDataSourceProvider();
    }

    @Bean
    @ConditionalOnMissingBean(DataSourceBuilder.class)
    @ConditionalOnClass(name = "com.zaxxer.hikari.HikariDataSource")
    public DataSourceBuilder dataSourceBuilder() {
        return new HikariDataSourceBuilder();
    }


    @Configuration
    static class DataSourceProviderConfiguration {
        DataSourceProviderConfiguration(List<DataSourceProvider> providers) {
            providers.forEach(DataSourceProviderHolder::registerProvider);
        }
    }
}
