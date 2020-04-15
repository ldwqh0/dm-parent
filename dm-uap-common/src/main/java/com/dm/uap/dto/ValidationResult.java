package com.dm.uap.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class ValidationResult implements Serializable {

    private static final long serialVersionUID = 579492257630348981L;

    private enum Result {
        /**
         * 成功
         */
        success,
        /**
         * 失败
         */
        failure
    }

    private final Result result;

    private final String message;

    public Result getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    private ValidationResult(Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public static ValidationResult success() {
        return new ValidationResult(Result.success, null);
    }

    public static ValidationResult failure() {
        return new ValidationResult(Result.failure, null);
    }

    public static ValidationResult success(String message) {
        return new ValidationResult(Result.success, message);
    }

    public static ValidationResult failure(String message) {
        return new ValidationResult(Result.failure, message);
    }

}
