package com.dm.uap.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class ResetPasswordRequest implements Serializable {
    private static final long serialVersionUID = -4966481409754529111L;

    private final String password;

    private final String rePassword;

    public ResetPasswordRequest(
        @JsonProperty("password") String password,
        @JsonProperty("rePassword") String rePassword) {
        this.password = password;
        this.rePassword = rePassword;
    }

    public String getPassword() {
        return password;
    }

    public String getRePassword() {
        return rePassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResetPasswordRequest that = (ResetPasswordRequest) o;
        return Objects.equals(password, that.password) && Objects.equals(rePassword, that.rePassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, rePassword);
    }
}
