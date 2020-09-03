package com.dm.datasource.converter;

import com.dm.collections.Maps;
import com.dm.common.converter.Converter;
import com.dm.datasource.dto.DmDataSourceDto;
import com.dm.datasource.entity.DmDataSource;
import com.dm.datasource.provider.DataSourceProperties;

import java.util.Map;

import javax.annotation.Nonnull;

public class DmDataSourceConverter implements Converter<DmDataSource, DmDataSourceDto> {

    @Override
    public DmDataSourceDto toDto(@Nonnull DmDataSource model) {
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
        DataSourceProperties properties = new DataSourceProperties();
        properties.setDatabase(from.getDatabase());
        properties.setDbType(from.getDbType());
        properties.setPassword(from.getPassword());
        properties.setPort(from.getPort());
        properties.setHost(from.getHost());
        properties.setUsername(from.getUsername());
        Map<String, String> additionalProperties = from.getProperties();
        if (Maps.isNotEmpty(additionalProperties)) {
            additionalProperties.forEach(properties::setProperty);
        }
        return properties;
    }

    public DataSourceProperties toDataSourceProperties(DmDataSource model) {
        DataSourceProperties properties = new DataSourceProperties();
        properties.setDatabase(model.getDatabase());
        properties.setDbType(model.getDbType());
        properties.setPassword(model.getPassword());
        properties.setPort(model.getPort());
        properties.setHost(model.getHost());
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
