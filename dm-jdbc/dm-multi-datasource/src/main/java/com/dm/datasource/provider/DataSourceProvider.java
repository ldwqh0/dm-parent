package com.dm.datasource.provider;

import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import org.hibernate.dialect.Dialect;

import java.util.Set;

public interface DataSourceProvider {

    String getDriverClassName();

    String getUrl(DataSourceProperties info);

    Dialect getDialect();

    default String getTestQuery() {
        return "select 1";
    }

    Set<DbTypes> getSupportDbTypes();
}
