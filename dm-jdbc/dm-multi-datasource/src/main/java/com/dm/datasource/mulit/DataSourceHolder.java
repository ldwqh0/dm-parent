package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;

import javax.sql.DataSource;

public interface DataSourceHolder {
    public void closeAndRemove(DataSourceProperties properties);

    public DataSource add(DataSourceProperties properties);
}
