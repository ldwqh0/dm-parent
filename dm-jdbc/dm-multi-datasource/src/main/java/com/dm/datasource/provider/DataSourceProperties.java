package com.dm.datasource.provider;

import java.util.Map;

public interface DataSourceProperties {

    public enum DbTypes {
        MySQL8,
        SQLSERVER
    }

    /**
     * 数据连接的类型
     *
     * @return
     */
    public DbTypes getDbType();

    public String getHost();

    public Integer getPort();

    public String getUsername();

    public String getPassword();

    public String getDatabase();

    public Map<String, String> getAdditionalProperties();

}
