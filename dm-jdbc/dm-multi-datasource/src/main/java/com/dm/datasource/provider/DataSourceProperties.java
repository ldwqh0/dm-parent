package com.dm.datasource.provider;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DataSourceProperties implements Serializable {

    private static final long serialVersionUID = 2331158044111375509L;

    private final DbTypes dbType;
    private final String host;
    private final Integer port;
    private final String username;
    private final String password;
    private final String database;
    private final Map<String, String> additionalProperties = new HashMap<>();

    public Map<String, String> getAdditionalProperties() {
        return Collections.unmodifiableMap(additionalProperties);
    }

    public enum DbTypes {
        MySQL8,
        SQLSERVER
    }

    public DataSourceProperties(DbTypes dbType, String host, Integer port, String username, String password, String database) {
        this.dbType = dbType;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
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


    public void setProperty(String key, String value) {
        this.additionalProperties.put(key, value);
    }

//    public String getStringKey() {
//
//    }

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
