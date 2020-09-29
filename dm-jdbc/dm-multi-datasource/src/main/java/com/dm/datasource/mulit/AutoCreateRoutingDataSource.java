package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;
import com.dm.datasource.provider.DataSourceProvider;
import com.dm.datasource.provider.DataSourceProviderHolder;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个可以自动创建数据源的 RoutingDatasource
 */
public class AutoCreateRoutingDataSource extends AbstractRoutingDataSource implements DataSourceHolder {

    private DataSource defaultTargetDataSource;

    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    @Override
    protected DataSource determineTargetDataSource() {
        DataSourceProperties properties = determineCurrentLookupKey();
        String key = properties.getKey().intern();
        DataSource resolved = null;
        if (Objects.nonNull(key)) {
            if ((resolved = dataSources.get(key)) == null) {
                synchronized (key) {
                    if ((resolved = dataSources.get(key)) == null) {
                        resolved = add(properties);
                    }
                }
            }
        }
        return Objects.isNull(resolved) ? defaultTargetDataSource : resolved;
    }

    private DataSource createDataSource(DataSourceProperties properties) {
        DataSourceProvider provider = DataSourceProviderHolder.getProvider(properties.getDbType());
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
        return DataSourceKeyContextHolder.determineCurrentLookupKey();
    }

    @Override
    public void afterPropertiesSet() {
        // 什么都不做
    }

    @Override
    public void closeAndRemove(DataSourceProperties properties) {
        DataSource dataSource = dataSources.get(properties);
        try {
            dataSources.remove(properties);
            if (dataSource instanceof HikariDataSource) {
                ((HikariDataSource) dataSource).close();
            }
        } catch (Exception e) {
            // 尝试关闭连接
            e.printStackTrace();
        }
    }

    @Override
    public DataSource add(DataSourceProperties properties) {
        final String key = properties.getKey().intern();
        DataSource exist = dataSources.get(key);
        if (exist == null) {
            synchronized (key) {
                exist = dataSources.get(key);
                if (exist == null) {
                    DataSource dataSource = createDataSource(properties);
                    dataSources.put(key, dataSource);
                    return dataSource;
                }
            }
        }
        return null;
    }

    // TODO 需要增加定时检测功能
}
