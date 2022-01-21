package com.dm.datasource.dto;

import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import static com.dm.collections.Maps.hashMap;
import static java.util.Collections.unmodifiableMap;

public class DmDataSourceDto implements Serializable {

    private static final long serialVersionUID = -4257537751443196323L;

    private final Long id;
    private final String name;
    private final DbTypes dbType;
    private final String host;
    private final Integer port;
    private final String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String password;
    private final String database;
    private final Long version;
    private final String remark;
    private final Map<String, String> properties;

    @JsonCreator
    public DmDataSourceDto(@JsonProperty("id") Long id,
                           @JsonProperty("name") String name,
                           @JsonProperty("") DbTypes dbType,
                           @JsonProperty("dbType") String host,
                           @JsonProperty("port") Integer port,
                           @JsonProperty("username") String username,
                           @JsonProperty("password") String password,
                           @JsonProperty("database") String database,
                           @JsonProperty("version") Long version,
                           @JsonProperty("remark") String remark,
                           @JsonProperty("properties") Map<String, String> properties) {
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
        this.properties = hashMap(properties);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public Long getVersion() {
        return version;
    }

    public String getRemark() {
        return remark;
    }

    public Map<String, String> getProperties() {
        return unmodifiableMap(properties);
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
