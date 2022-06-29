package com.dm.security.verification;

import java.io.Serializable;

/**
 * 设备信息
 */
public interface DeviceInfo extends Serializable {
    /**
     * 设备识别码
     */
    String getDeviceKey();
}
