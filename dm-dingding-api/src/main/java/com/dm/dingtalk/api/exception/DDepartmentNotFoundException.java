package com.dm.dingtalk.api.exception;

public class DDepartmentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -8763247141763252023L;

    public DDepartmentNotFoundException(String message) {
        super(message);
    }
}
