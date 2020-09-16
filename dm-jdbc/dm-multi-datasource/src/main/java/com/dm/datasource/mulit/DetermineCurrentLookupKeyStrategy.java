package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;

/**
 * Datasource选择策略
 */
public interface DetermineCurrentLookupKeyStrategy {

    /**
     * 选择一个
     * 
     * @return
     */
    public DataSourceProperties determineCurrentLookupKey();

    public void setKey(DataSourceProperties properties);

    public void clear();
}
