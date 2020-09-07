package com.dm.datasource.provider;

import com.dm.collections.Sets;
import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQL8Dialect;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MySQL8DataSourceProvider implements DataSourceProvider {

    private final Dialect dialect = new MySQL8Dialect();

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public Set<DbTypes> getSupportDbTypes() {
        return Sets.hashSet(DbTypes.MySQL8);
    }

    @Override
    public String getDriverClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String getUrl(DataSourceProperties info) {
        Map<String, String> additionalProperties = info.getAdditionalProperties();
        String query = StringUtils.join("?", additionalProperties.entrySet().stream().map(entry -> StringUtils.join(entry.getKey(), "=", entry.getValue()))
            .collect(Collectors.joining("&")));
        return StringUtils.join(
            "jdbc:mysql://",
            info.getHost(),
            ":",
            info.getPort(),
            "/",
            info.getDatabase(),
            query);
    }
}
