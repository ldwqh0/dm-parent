package com.dm.datasource.provider;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dm.collections.Lists.arrayList;
import static com.dm.collections.Maps.hashMap;
import static java.util.Collections.unmodifiableMap;

public final class DataSourceProperties implements Serializable {

    private static final long serialVersionUID = 2331158044111375509L;

    private final DbTypes dbType;
    private final String host;
    private final Integer port;
    private final String username;
    private final String password;
    private final String database;
    private final Map<String, String> additionalProperties;

    private final String key;

    public Map<String, String> getAdditionalProperties() {
        return unmodifiableMap(additionalProperties);
    }

    public enum DbTypes {
        MySQL8,
        SQLSERVER,
        DB2,
        Derby,
        H2,
        HANA,
        HSQL,
        Informix,
        Ingres,
        Interbase,
        MariaDB,
        Oracle,
        PostgreSQL,
        Progress,
        Sybase,
        Teradata,
        /**
         * 达梦数据库
         */
        DM,
        /**
         * 人大金仓数据库
         */
        Kingbase,
        /**
         * 南大通用数据库
         */
        GBase
    }

    public DataSourceProperties(DbTypes dbType, String host, Integer port, String username, String password, String database, Map<String, String> additionalProperties) {
        this.dbType = dbType;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.additionalProperties = hashMap(additionalProperties);
        this.key = createStringKey();
    }

    public DbTypes getDbType() {
        return dbType;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    private String createStringKey() {
        List<String> keys = arrayList(additionalProperties.keySet());
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            builder.append(key).append(":").append(additionalProperties.get(key)).append(",");
        }
        return DigestUtils.md5DigestAsHex(StringUtils.join(dbType, host, port, username, password, database, builder.toString()).getBytes(StandardCharsets.UTF_8));
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSourceProperties that = (DataSourceProperties) o;
        return dbType == that.dbType &&
            Objects.equals(host, that.host) &&
            Objects.equals(port, that.port) &&
            Objects.equals(username, that.username) &&
            Objects.equals(password, that.password) &&
            Objects.equals(database, that.database) &&
            Objects.equals(additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbType, host, port, username, password, database, additionalProperties);
    }
}
