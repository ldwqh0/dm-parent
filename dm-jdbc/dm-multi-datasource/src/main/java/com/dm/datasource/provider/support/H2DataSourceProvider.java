package com.dm.datasource.provider.support;

import com.dm.datasource.provider.DataSourceProperties;
import com.dm.datasource.provider.DataSourceProvider;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;

import java.util.Collections;
import java.util.Set;

public class H2DataSourceProvider implements DataSourceProvider {

    private final Dialect dialect = new H2Dialect();

    @Override
    public String getDriverClassName() {
        return "org.h2.Driver";
    }

    @Override
    public String getUrl(DataSourceProperties info) {
        return StringUtils.join(
            "jdbc:h2:",
            info.getHost(),
            "/",
            info.getDatabase()
        );
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public Set<DataSourceProperties.DbTypes> getSupportDbTypes() {
        return Collections.singleton(DataSourceProperties.DbTypes.H2);
    }
}
