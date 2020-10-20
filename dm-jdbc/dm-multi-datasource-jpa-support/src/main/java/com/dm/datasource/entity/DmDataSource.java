package com.dm.datasource.entity;

import com.dm.common.entity.AbstractEntity;
import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter
@Setter
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

}
