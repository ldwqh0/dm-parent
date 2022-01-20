package com.dm.datasource.entity;

import com.dm.data.domain.AbstractEntity;
import com.dm.datasource.provider.DataSourceProperties.DbTypes;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;

import static com.dm.collections.Maps.hashMap;

@Entity
@Table(name = "dm_datasource_")
public class DmDataSource extends AbstractEntity {

    @Column(name = "name_", length = 50)
    private String name;

    @Column(name = "host_", length = 100, nullable = false)
    private String host;

    @Column(name = "port_", nullable = false)
    private Integer port;

    @Column(name = "db_type_", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private DbTypes dbType;

    @Column(name = "username_", length = 50, nullable = false)
    private String username;

    @Column(name = "password_", length = 50)
    private String password;

    @Column(name = "database_", length = 50)
    private String database;

    @CollectionTable(name = "dm_datasource_property_")
    @MapKeyColumn(name = "key_")
    @Column(name = "value_")
    @ElementCollection
    private Map<String, String> properties;

    @Column(name = "remark_")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String remark;

    public DmDataSource() {
    }

    public DmDataSource(String name, String host, Integer port, DbTypes dbType, String username, String password, String database, Map<String, String> properties, String remark) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.dbType = dbType;
        this.username = username;
        this.password = password;
        this.database = database;
        this.properties = hashMap(properties);
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DmDataSource that = (DmDataSource) o;
        return Objects.equals(name, that.name) && Objects.equals(host, that.host) && Objects.equals(port, that.port) && dbType == that.dbType && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(database, that.database) && Objects.equals(properties, that.properties) && Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, host, port, dbType, username, password, database, properties, remark);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public DbTypes getDbType() {
        return dbType;
    }

    public void setDbType(DbTypes dbType) {
        this.dbType = dbType;
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

    public Map<String, String> getProperties() {
        return hashMap(properties);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = hashMap(properties);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DmDataSource{" +
            "name='" + name + '\'' +
            ", host='" + host + '\'' +
            ", port=" + port +
            ", dbType=" + dbType +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", database='" + database + '\'' +
            ", properties=" + properties +
            ", remark='" + remark + '\'' +
            '}';
    }
}
