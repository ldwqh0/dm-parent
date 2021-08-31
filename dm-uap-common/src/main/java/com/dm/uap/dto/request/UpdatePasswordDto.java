package com.dm.uap.dto.request;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class UpdatePasswordDto implements Serializable {
    private static final long serialVersionUID = -7352479163299106344L;
    @NotBlank
    private String oldPassword;

    @NotBlank
    private String password;

    @NotBlank
    private String repassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdatePasswordDto that = (UpdatePasswordDto) o;
        return Objects.equals(oldPassword, that.oldPassword) && Objects.equals(password, that.password) && Objects.equals(repassword, that.repassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldPassword, password, repassword);
    }
}
