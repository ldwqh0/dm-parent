package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;

/**
 * Datasource选择策略
 */
public interface DetermineCurrentLookupKeyStrategy {

    /**
     * 选择一个
     *
     * @return 一个 {@link DataSourceProperties}
     */
    DataSourceProperties determineCurrentLookupKey();

    void setKey(DataSourceProperties properties);

    void clear();
}
