package com.dm.datasource.dto;

import java.io.Serializable;

public class Property implements Serializable {
    private static final long serialVersionUID = -8933629063315436930L;

    private final String key;
    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
