package com.dm.datasource.dto;

import com.dm.datasource.provider.DataSourceProperties.DbTypes;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class DmDataSourceDto implements Serializable {
    private static final long serialVersionUID = -4257537751443196323L;
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
    private Map<String, String> properties;

    public void setProperty(String key, String value) {
        if (properties == null) {
            properties = new HashMap<String, String>();
        }
        properties.put(key, value);
    }
}
