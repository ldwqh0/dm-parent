package com.dm.jdbc;

import java.io.Serializable;

public interface CheckResult {
    Boolean getResult();

    String getMessage();

    static CheckResult success() {
        return new CheckResultImpl(Boolean.TRUE);
    }

    static CheckResult failure(Exception ex) {
        return new CheckResultImpl(Boolean.FALSE, ex.getMessage());
    }

    class CheckResultImpl implements CheckResult, Serializable {
        private static final long serialVersionUID = 3836161032556759081L;
        private final Boolean result;
        private final String message;

        public Boolean getResult() {
            return result;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public CheckResultImpl(Boolean result) {
            this(result, null);
        }

        public CheckResultImpl(Boolean result, String message) {
            this.result = result;
            this.message = message;
        }
    }
}
