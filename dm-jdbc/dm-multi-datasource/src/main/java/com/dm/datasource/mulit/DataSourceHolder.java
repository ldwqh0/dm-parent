package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;

import javax.sql.DataSource;

public interface DataSourceHolder {
    void closeAndRemove(DataSourceProperties properties);

    DataSource add(DataSourceProperties properties);
}
