package com.dm.datasource;

import com.dm.datasource.provider.DataSourceProperties;

import javax.sql.DataSource;

public interface DataSourceBuilder {
    DataSource buildDataSource(DataSourceProperties properties);
}
