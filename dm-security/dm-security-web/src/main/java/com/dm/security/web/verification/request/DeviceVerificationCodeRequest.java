package com.dm.security.web.verification.request;

import com.dm.security.verification.DeviceInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceVerificationCodeRequest implements DeviceInfo {
    private static final long serialVersionUID = 7810055392588299209L;

    /**
     * 设备唯一识别码
     */
    private final String deviceKey;

    @JsonCreator
    public DeviceVerificationCodeRequest(@JsonProperty("deviceKey") String deviceKey) {
        this.deviceKey = deviceKey;
    }

    @Override
    public String getDeviceKey() {
        return deviceKey;
    }
}
