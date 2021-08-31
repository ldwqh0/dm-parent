package com.dm.datasource.dto;

import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DmDataSourceDto implements Serializable {
    public DmDataSourceDto() {
    }

    public DmDataSourceDto(Long id, String name, DbTypes dbType, String host, Integer port, String username, String password, String database, Long version, String remark, Map<String, String> properties) {
        this.id = id;
        this.name = name;
        this.dbType = dbType;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.version = version;
        this.remark = remark;
        this.properties = properties;
    }

    private static final long serialVersionUID = -4257537751443196323L;
    private Long id;
    private String name;
    private DbTypes dbType;
    private String host;
    private Integer port;
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String database;
    private Long version;
    private String remark;
    private Map<String, String> properties;

    public void setProperty(String key, String value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DmDataSourceDto that = (DmDataSourceDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && dbType == that.dbType && Objects.equals(host, that.host) && Objects.equals(port, that.port) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(database, that.database) && Objects.equals(version, that.version) && Objects.equals(remark, that.remark) && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dbType, host, port, username, password, database, version, remark, properties);
    }

    @Override
    public String toString() {
        return "DmDataSourceDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", dbType=" + dbType +
            ", host='" + host + '\'' +
            ", port=" + port +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", database='" + database + '\'' +
            ", version=" + version +
            ", remark='" + remark + '\'' +
            ", properties=" + properties +
            '}';
    }
}
