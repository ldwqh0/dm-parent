package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;
import com.dm.datasource.provider.DataSourceProvider;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个可以自动创建数据源的 RoutingDatasource
 */
public class AutoCreateRoutingDatasource extends AbstractRoutingDataSource {

    private DetermineCurrentLookupKeyStrategy strategy;

    private DataSource defaultTargetDataSource;

    private Map<String, DataSourceProvider> dataSourceProviders = new ConcurrentHashMap<>();

    private final Map<DataSourceProperties, DataSource> dataSources = new ConcurrentHashMap<>();

    public void setStrategy(DetermineCurrentLookupKeyStrategy strategy) {
        this.strategy = strategy;
    }

    public void setDataSourceProviders(Iterable<DataSourceProvider> dataSourceProviders) {
        dataSourceProviders.forEach(dataSourceProvider -> {
            dataSourceProvider.getSupportDbTypes().forEach(dbType -> {
                this.dataSourceProviders.put(dbType, dataSourceProvider);
            });
        });
    }

    @Override
    protected DataSource determineTargetDataSource() {
        DataSourceProperties key = determineCurrentLookupKey();
        DataSource resolved = null;
        if (!Objects.isNull(key)) {
            resolved = dataSources.get(key);
            if (resolved == null) {
                resolved = tryCreateNewDataSource(key);
            }
        }
        return Objects.isNull(resolved) ? defaultTargetDataSource : resolved;
    }

    private synchronized DataSource tryCreateNewDataSource(DataSourceProperties key) {
        DataSource exist = dataSources.get(key);
        if (exist == null) {
            DataSource dataSource = createDataSource(key);
            if (!Objects.isNull(dataSource)) {
                dataSources.put(key, dataSource);
                return dataSource;
            }
        }
        return null;
    }

    private DataSource createDataSource(DataSourceProperties properties) {
        DataSourceProvider provider = dataSourceProviders.get(properties.getDbType());
        if (Objects.isNull(provider)) {
            return null;
        } else {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(provider.getDriverClassName());
            dataSource.setJdbcUrl(provider.getUrl(properties));
            dataSource.setUsername(properties.getUsername());
            dataSource.setPassword(properties.getPassword());
            dataSource.setConnectionTestQuery("select 1");
            return dataSource;
        }
    }


    public void setDefaultTargetDataSource(DataSource defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        this.defaultTargetDataSource = defaultTargetDataSource;
    }

    @Override
    protected DataSourceProperties determineCurrentLookupKey() {
        return strategy.determineCurrentLookupKey();
    }

    @Override
    public void afterPropertiesSet() {
        // 什么都不做
    }

    // TODO 需要增加定时检测功能
}

