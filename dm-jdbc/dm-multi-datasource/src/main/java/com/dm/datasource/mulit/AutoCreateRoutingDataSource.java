package com.dm.datasource.mulit;

import com.dm.datasource.DataSourceBuilder;
import com.dm.datasource.HikariDataSourceBuilder;
import com.dm.datasource.provider.DataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
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

    private DataSourceBuilder dataSourceBuilder = new HikariDataSourceBuilder();

    @Autowired(required = false)
    public void setDataSourceBuilder(DataSourceBuilder dataSourceBuilder) {
        this.dataSourceBuilder = dataSourceBuilder;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        DataSourceProperties properties = determineCurrentLookupKey();
        DataSource resolved = null;
        if (Objects.nonNull(properties)) {
            String key = properties.getKey().intern();
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
        try {
            if (properties != null) {
                String key = properties.getKey();
                DataSource dataSource = dataSources.remove(key);
                if (Objects.nonNull(dataSource) && dataSource instanceof HikariDataSource) {
                    ((HikariDataSource) dataSource).close();
                }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("尝试关闭连接池时发生错误", e);
            }
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
                    DataSource dataSource = dataSourceBuilder.buildDataSource(properties);
                    dataSources.put(key, dataSource);
                    return dataSource;
                }
            }
        }
        return null;
    }

    // TODO 需要增加定时检测功能
}
