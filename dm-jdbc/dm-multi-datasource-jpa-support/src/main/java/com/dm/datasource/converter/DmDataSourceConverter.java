package com.dm.datasource.converter;

import com.dm.collections.Maps;
import com.dm.common.converter.Converter;
import com.dm.datasource.dto.DmDataSourceDto;
import com.dm.datasource.entity.DmDataSource;
import com.dm.datasource.provider.DataSourceProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DmDataSourceConverter implements Converter<DmDataSource, DmDataSourceDto> {

    @Override
    public DmDataSourceDto toDto(DmDataSource model) {
        DmDataSourceDto dto = toSimpleDto(model);
        Map<String, String> additionalProperties = model.getProperties();
        if (Maps.isNotEmpty(additionalProperties)) {
            additionalProperties.forEach(dto::setProperty);
        }
        return dto;
    }

    @Override
    public DmDataSource copyProperties(DmDataSource model, DmDataSourceDto source) {
        model.setName(source.getName());
        model.setDatabase(source.getDatabase());
        model.setDbType(source.getDbType());
        model.setHost(source.getHost());
        model.setPassword(source.getPassword());
        model.setPort(source.getPort());
        model.setProperties(source.getProperties());
        model.setRemark(source.getRemark());
        model.setUsername(source.getUsername());
        model.setVersion(source.getVersion());
        return model;
    }

    public DataSourceProperties toDataSourceProperties(DmDataSourceDto from) {
        DataSourcePropertiesDto properties = new DataSourcePropertiesDto();
        properties.setDatabase(from.getDatabase());
        properties.setDbType(from.getDbType());
        properties.setPassword(from.getPassword());
        properties.setPort(from.getPort());
        properties.setServer(from.getHost());
        properties.setUsername(from.getUsername());
        Map<String, String> additionalProperties = from.getProperties();
        if (Maps.isNotEmpty(additionalProperties)) {
            additionalProperties.forEach(properties::setProperty);
        }
        return properties;
    }

    public DataSourceProperties toDataSourceProperties(DmDataSource model) {
        DataSourcePropertiesDto properties = new DataSourcePropertiesDto();
        properties.setDatabase(model.getDatabase());
        properties.setDbType(model.getDbType());
        properties.setPassword(model.getPassword());
        properties.setPort(model.getPort());
        properties.setServer(model.getHost());
        properties.setUsername(model.getUsername());
        Map<String, String> additionalProperties = model.getProperties();
        if (Maps.isNotEmpty(additionalProperties)) {
            additionalProperties.forEach(properties::setProperty);
        }
        return properties;
    }

    public DmDataSourceDto toSimpleDto(DmDataSource model) {
        DmDataSourceDto dto = new DmDataSourceDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDatabase(model.getDatabase());
        dto.setDbType(model.getDbType());
        dto.setPassword(model.getPassword());
        dto.setPort(model.getPort());
        dto.setHost(model.getHost());
        dto.setUsername(model.getUsername());
        dto.setVersion(model.getVersion());
        return dto;
    }
}

class DataSourcePropertiesDto implements DataSourceProperties {

    private DbTypes dbType;
    private String server;
    private Integer port;
    private String username;
    private String password;
    private String database;
    private Map<String, String> additionalProperties = new HashMap<>();


    @Override
    public DbTypes getDbType() {
        return dbType;
    }

    @Override
    public String getHost() {
        return server;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setDbType(DbTypes dbType) {
        this.dbType = dbType;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setProperty(String property, String value) {
        this.additionalProperties.put(property, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSourcePropertiesDto that = (DataSourcePropertiesDto) o;
        return Objects.equals(dbType, that.dbType) &&
            Objects.equals(server, that.server) &&
            Objects.equals(port, that.port) &&
            Objects.equals(username, that.username) &&
            Objects.equals(password, that.password) &&
            Objects.equals(database, that.database) &&
            Objects.equals(additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbType, server, port, username, password, database, additionalProperties);
    }
}
