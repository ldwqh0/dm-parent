package com.dm.datasource.provider;

import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import org.hibernate.dialect.Dialect;

import java.util.Set;

public interface DataSourceProvider {
    default String getDriverClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    String getUrl(DataSourceProperties info);

    Dialect getDialect();

    Set<DbTypes> getSupportDbTypes();
}
