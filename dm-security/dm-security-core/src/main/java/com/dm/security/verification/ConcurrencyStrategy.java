package com.dm.security.verification;

/**
 * 验证码并发策略
 */
public interface ConcurrencyStrategy {
    // 尝试获取设备锁
    void tryLock(DeviceInfo device);
}
