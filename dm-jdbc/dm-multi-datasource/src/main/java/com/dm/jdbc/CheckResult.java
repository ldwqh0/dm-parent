package com.dm.jdbc;

import java.io.Serializable;

public interface CheckResult {
    Boolean getResult();

    String getMessage();

    static CheckResult success() {
        return new CheckResultImpl(Boolean.TRUE, null);
    }

    static CheckResult failure(Exception ex) {
        return new CheckResultImpl(Boolean.FALSE, ex.getMessage());
    }

    class CheckResultImpl implements CheckResult, Serializable {
        private final Boolean result;
        private final String message;

        public Boolean getResult() {
            return result;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public CheckResultImpl(Boolean result, String message) {
            this.result = result;
            this.message = message;
        }
    }
}
