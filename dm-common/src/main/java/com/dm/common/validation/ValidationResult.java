package com.dm.common.validation;

import java.io.Serializable;
import java.util.Objects;

/**
 * 表示一个验证结果
 */
public interface ValidationResult extends Serializable {

    /**
     * 验证是否成功
     */
    boolean isSuccess();

    /**
     * 验证的错误消息 (如果有)
     */
    String getMessage();

    static ValidationResult success() {
        return new SimpleValidationResult(true, null);
    }

    static ValidationResult failure() {
        return new SimpleValidationResult(false, null);
    }

    static ValidationResult success(String message) {
        return new SimpleValidationResult(true, message);
    }

    static ValidationResult failure(String message) {
        return new SimpleValidationResult(false, message);
    }

}

final class SimpleValidationResult implements ValidationResult {

    private static final long serialVersionUID = 2315515054733173232L;

    private final boolean success;

    private final String message;

    public SimpleValidationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleValidationResult that = (SimpleValidationResult) o;
        return success == that.success && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message);
    }
}
