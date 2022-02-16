package com.dm.datasource.dto;

import com.dm.collections.Sets;
import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.dm.collections.Sets.hashSet;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static java.util.Collections.unmodifiableSet;

public class DmDataSourceDto implements Serializable {

    private static final long serialVersionUID = -4257537751443196323L;
    @JsonProperty(access = READ_ONLY)
    private final Long id;
    private final String name;
    private final DbTypes dbType;
    private final String host;
    private final Integer port;
    private final String username;

    @JsonProperty(access = WRITE_ONLY)
    private final String password;
    private final String database;
    private final Long version;
    private final String remark;
    private final Set<Property> properties;

    private DmDataSourceDto(Long id,
                            String name,
                            DbTypes dbType,
                            String host,
                            Integer port,
                            String username,
                            String password,
                            String database,
                            Long version,
                            String remark,
                            Set<Property> properties) {
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
        this.properties = hashSet(properties);
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

    public Set<Property> getProperties() {
        return unmodifiableSet(properties);
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private String name;
        private DbTypes dbType;
        private String host;
        private Integer port;
        private String username;
        private String password;
        private String database;
        private Long version;
        private String remark;
        private Set<Property> properties;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder dbType(DbTypes dbType) {
            this.dbType = dbType;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public Builder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public Builder properties(Map<String, String> properties) {
            this.properties = Sets.transform(properties.entrySet(), it -> new Property(it.getKey(), it.getValue()));
            return this;
        }

        public Builder properties(Set<Property> properties) {
            this.properties = properties;
            return this;
        }

        public DmDataSourceDto build() {
            return new DmDataSourceDto(id, name, dbType, host, port, username, password, database, version, remark, properties);
        }
    }
}
