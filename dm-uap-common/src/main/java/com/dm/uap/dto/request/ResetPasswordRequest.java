package com.dm.uap.dto.request;

import java.io.Serializable;
import java.util.Objects;

public class ResetPasswordRequest implements Serializable {

    private String password;

    private String rePassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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
