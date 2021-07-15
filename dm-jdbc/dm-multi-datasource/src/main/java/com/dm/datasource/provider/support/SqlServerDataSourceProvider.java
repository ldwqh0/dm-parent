package com.dm.datasource.provider.support;

import com.dm.datasource.provider.DataSourceProperties;
import com.dm.datasource.provider.DataSourceProvider;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServer2012Dialect;

import java.util.Collections;
import java.util.Set;

public class SqlServerDataSourceProvider implements DataSourceProvider {

    private final Dialect dialect = new SQLServer2012Dialect();

    @Override
    public String getDriverClassName() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    public String getUrl(DataSourceProperties info) {
        return StringUtils.join(
            "jdbc:sqlserver://",
            info.getHost(),
            ":",
            info.getPort(),
            ";databaseName=",
            info.getDatabase()
        );
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public Set<DataSourceProperties.DbTypes> getSupportDbTypes() {
        return Collections.singleton(DataSourceProperties.DbTypes.SQLSERVER);
    }
}
