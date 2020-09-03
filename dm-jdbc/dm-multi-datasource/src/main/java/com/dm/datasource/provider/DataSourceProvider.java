package com.dm.datasource.provider;

import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import org.hibernate.dialect.Dialect;

import java.util.Set;

public interface DataSourceProvider {
    String getDriverClassName();

    public String getUrl(DataSourceProperties info);

    public Dialect getDialect();

    public Set<DbTypes> getSupportDbTypes();
}
