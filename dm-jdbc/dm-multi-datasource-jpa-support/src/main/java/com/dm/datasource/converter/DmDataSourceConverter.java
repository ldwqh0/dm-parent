package com.dm.datasource.converter;

import com.dm.collections.Maps;
import com.dm.datasource.dto.DmDataSourceDto;
import com.dm.datasource.dto.Property;
import com.dm.datasource.entity.DmDataSource;
import com.dm.datasource.provider.DataSourceProperties;

public final class DmDataSourceConverter {

    private DmDataSourceConverter() {
    }

    private static DmDataSourceDto.Builder newBuilder(DmDataSource model) {
        return DmDataSourceDto.builder()
            .id(model.getId())
            .name(model.getName())
            .dbType(model.getDbType())
            .host(model.getHost())
            .port(model.getPort())
            .username(model.getUsername())
            .password(model.getPassword())
            .database(model.getDatabase())
            .version(model.getVersion());
    }

    public static DmDataSourceDto toDto(DmDataSource model) {
        return newBuilder(model)
            .properties(model.getProperties())
            .remark(model.getRemark())
            .build();

    }

    public static DataSourceProperties toDataSourceProperties(DmDataSourceDto from) {
        return new DataSourceProperties(
            from.getDbType(),
            from.getHost(),
            from.getPort(),
            from.getUsername(),
            from.getPassword(),
            from.getDatabase(),
            Maps.map(from.getProperties(), Property::getKey, Property::getValue)
        );
    }

    public static DataSourceProperties toDataSourceProperties(DmDataSource model) {
        return new DataSourceProperties(
            model.getDbType(),
            model.getHost(),
            model.getPort(),
            model.getUsername(),
            model.getPassword(),
            model.getDatabase(),
            model.getProperties()
        );
    }

    public static DmDataSourceDto toSimpleDto(DmDataSource model) {
        return newBuilder(model).build();
    }
}
