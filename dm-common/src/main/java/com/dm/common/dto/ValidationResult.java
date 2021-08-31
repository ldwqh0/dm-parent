package com.dm.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(value = Include.NON_EMPTY)
public class ValidationResult implements Serializable {

    private static final long serialVersionUID = 579492257630348981L;

    private final boolean success;

    private final String message;

    public String getMessage() {
        return message;
    }

    private ValidationResult(boolean result, String message) {
        this.success = result;
        this.message = message;
    }

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult failure() {
        return new ValidationResult(false, null);
    }

    public static ValidationResult success(String message) {
        return new ValidationResult(true, message);
    }

    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationResult that = (ValidationResult) o;
        return success == that.success && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message);
    }

    public boolean isSuccess() {
        return success;
    }
}
