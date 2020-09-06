package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;

public final class ThreadLocalDetermineCurrentLookupKeyStrategy implements DetermineCurrentLookupKeyStrategy {

    private static ThreadLocal<DataSourceProperties> threadLocal = new ThreadLocal<>();

    @Override
    public DataSourceProperties determineCurrentLookupKey() {
        return threadLocal.get();
    }

    @Override
    public void clear() {
        threadLocal.remove();
    }

    @Override
    public void setKey(DataSourceProperties properties) {
        threadLocal.set(properties);
    }
}
