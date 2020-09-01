package com.dm.datasource.provider;

import java.util.Map;

public interface DataSourceProperties {

    /**
     * 数据连接的类型
     *
     * @return
     */
    public String getDbType();

    public String getHost();

    public Integer getPort();

    public String getUsername();

    public String getPassword();

    public String getDatabase();

    public Map<String, String> getAdditionalProperties();

}
