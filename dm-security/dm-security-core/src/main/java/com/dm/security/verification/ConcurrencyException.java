package com.dm.security.verification;

/**
 * 尝试获取设备锁时发送该异常
 */
public class ConcurrencyException extends RuntimeException {
    private static final long serialVersionUID = -8922944591202219514L;

    public ConcurrencyException() {
        this("设备验证码间隔时间太短");
    }

    public ConcurrencyException(String message) {
        super(message);
    }
}
