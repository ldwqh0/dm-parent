package com.dm.datasource.mulit;

import com.dm.datasource.provider.DataSourceProperties;

public class ThreadLocalDetermineCurrentLookupKeyStrategy implements DetermineCurrentLookupKeyStrategy {

    private ThreadLocal<DataSourceProperties> threadLocal = new ThreadLocal<>();

    public void putKey(DataSourceProperties properties) {
        threadLocal.set(properties);
    }


    @Override
    public DataSourceProperties determineCurrentLookupKey() {
        return threadLocal.get();
    }

    public void clear() {
        threadLocal.remove();
    }
}
