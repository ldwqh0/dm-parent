package com.dm.dingtalk.api.callback.model;

import java.util.HashMap;

public class Event extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public String getType() {
        return get("EventType").toString();
    }
}
