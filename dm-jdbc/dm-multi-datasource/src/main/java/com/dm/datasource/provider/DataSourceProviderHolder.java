package com.dm.datasource.provider;

import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import com.dm.jdbc.ConnectionUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DataSourceProviderHolder {

    private static final Map<DataSourceProperties.DbTypes, DataSourceProvider> providers = new ConcurrentHashMap<>();

    private DataSourceProviderHolder() {

    }

    public static void registerProvider(DataSourceProvider provider) {
        provider.getSupportDbTypes().forEach(dbType -> providers.put(dbType, provider));
    }

    public static DataSourceProvider getProvider(DataSourceProperties properties) {
        return providers.get(properties.getDbType());
    }

    public static DataSourceProvider getProvider(DataSourceProperties.DbTypes type) {
        return providers.get(type);
    }

    public static Connection getConnection(DataSourceProperties properties)
        throws SQLException, ClassNotFoundException {
        DbTypes dbType = properties.getDbType();
        DataSourceProvider provider = getProvider(dbType);
        return ConnectionUtils.createConnection(provider.getUrl(properties), provider.getDriverClassName(),
            properties.getUsername(), properties.getPassword());
    }
}
