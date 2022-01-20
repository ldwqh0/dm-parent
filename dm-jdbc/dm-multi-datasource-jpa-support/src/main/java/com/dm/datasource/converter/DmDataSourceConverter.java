package com.dm.datasource.converter;

import com.dm.datasource.dto.DmDataSourceDto;
import com.dm.datasource.entity.DmDataSource;
import com.dm.datasource.provider.DataSourceProperties;

public final class DmDataSourceConverter {

    public static DmDataSourceDto toDto(DmDataSource model) {
        return new DmDataSourceDto(
            model.getId(),
            model.getName(),
            model.getDbType(),
            model.getHost(),
            model.getPort(),
            model.getUsername(),
            model.getPassword(),
            model.getDatabase(),
            model.getVersion(),
            model.getRemark(),
            model.getProperties()
        );
    }

    public static DataSourceProperties toDataSourceProperties(DmDataSourceDto from) {
        return new DataSourceProperties(
            from.getDbType(),
            from.getHost(),
            from.getPort(),
            from.getUsername(),
            from.getPassword(),
            from.getDatabase(),
            from.getProperties()
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
        return new DmDataSourceDto(
            model.getId(),
            model.getName(),
            model.getDbType(),
            model.getHost(),
            model.getPort(),
            model.getUsername(),
            model.getPassword(),
            model.getDatabase(),
            model.getVersion(),
            "",
            null
        );
    }
}
