package com.dm.datasource.provider.support;

import com.dm.collections.Sets;
import com.dm.datasource.provider.DataSourceProperties;
import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import com.dm.datasource.provider.DataSourceProvider;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle12cDialect;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OracleDataSourceProvider implements DataSourceProvider {

    private final Dialect dialect = new Oracle12cDialect();

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public Set<DbTypes> getSupportDbTypes() {
        return Sets.hashSet(DbTypes.Oracle);
    }

    @Override
    public String getDriverClassName() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    @Override
    public String getTestQuery() {
        return "select 1 from dual";
    }

    @Override
    public String getUrl(DataSourceProperties info) {
        Map<String, String> additionalProperties = info.getAdditionalProperties();
        String query = "";
        if (!additionalProperties.isEmpty()) {
            query = StringUtils.join("?", additionalProperties.entrySet().stream().map(entry -> StringUtils.join(entry.getKey(), "=", entry.getValue()))
                .collect(Collectors.joining("&")));
        }
        return StringUtils.join(
            "jdbc:oracle:thin:@",
            info.getHost(),
            ":",
            info.getPort(),
            ":",
            info.getDatabase(),
            query);
    }
}
