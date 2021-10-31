package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;

public interface DataSourceHolder {
    void closeAndRemove(DataSourceProperties properties);
}
