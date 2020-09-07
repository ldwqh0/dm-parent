package com.dm.datasource.provider;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class DataSourceProperties implements Serializable {

    private static final long serialVersionUID = 2331158044111375509L;

    private DbTypes dbType;
    private String host;
    private Integer port;
    private String username;

    private String password;
    private String database;
    private Map<String, String> additionalProperties = new HashMap<String, String>();

    public Map<String, String> getAdditionalProperties() {
        return Collections.unmodifiableMap(additionalProperties);
    }

    public enum DbTypes {
        MySQL8,
        SQLSERVER
    }

    public DbTypes getDbType() {
        return dbType;
    }

    public void setDbType(DbTypes dbType) {
        this.dbType = dbType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setProperty(String key, String value) {
        this.additionalProperties.put(key, value);
    }

}
