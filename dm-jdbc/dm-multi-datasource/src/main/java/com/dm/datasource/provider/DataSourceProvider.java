package com.dm.datasource.provider;

import org.hibernate.dialect.Dialect;

import java.util.Set;

public interface DataSourceProvider {
    String getDriverClassName();

    public String getUrl(DataSourceProperties info);

    public Dialect getDialect();

    public Set<String> getSupportDbTypes();
}
