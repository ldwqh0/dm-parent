package com.dm.datasource.provider;

import com.dm.collections.Lists;
import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import com.dm.jdbc.ConnectionUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataSourceProviderHolder {

    private static Map<DataSourceProperties.DbTypes, DataSourceProvider> providers;

    static {
        initialize();
    }

    private DataSourceProviderHolder() {

    }

    private static void initialize() {
        List<DataSourceProvider> providers = Lists.arrayList(new MySQL8DataSourceProvider());
        Map<DataSourceProperties.DbTypes, DataSourceProvider> dpm = new HashMap<>();
        providers.forEach(provider -> provider.getSupportDbTypes().forEach(dbType -> dpm.put(dbType, provider)));
        DataSourceProviderHolder.providers = Collections.unmodifiableMap(dpm);
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
