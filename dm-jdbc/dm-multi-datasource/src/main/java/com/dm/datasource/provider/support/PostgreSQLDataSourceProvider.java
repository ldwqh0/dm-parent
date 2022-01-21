package com.dm.datasource.provider.support;

import com.dm.datasource.provider.DataSourceProperties;
import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import com.dm.datasource.provider.DataSourceProvider;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL95Dialect;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

public class PostgreSQLDataSourceProvider implements DataSourceProvider {

    private final Dialect dialect = new PostgreSQL95Dialect();

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public Set<DbTypes> getSupportDbTypes() {
        return singleton(DbTypes.PostgreSQL);
    }

    @Override
    public String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getUrl(DataSourceProperties info) {
        Map<String, String> additionalProperties = info.getAdditionalProperties();
        String query = StringUtils.join("?", additionalProperties.entrySet().stream().map(entry -> StringUtils.join(entry.getKey(), "=", entry.getValue()))
            .collect(Collectors.joining("&")));
        return StringUtils.join(
            "jdbc:postgresql://",
            info.getHost(),
            ":",
            info.getPort(),
            "/",
            info.getDatabase(),
            query);
    }
}
