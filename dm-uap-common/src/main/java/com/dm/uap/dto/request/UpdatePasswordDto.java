package com.dm.uap.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class UpdatePasswordDto implements Serializable {
    private static final long serialVersionUID = -7352479163299106344L;

    public UpdatePasswordDto(
        @JsonProperty("oldPassword") String oldPassword,
        @JsonProperty("password") String password,
        @JsonProperty("repassword") String repassword) {
        this.oldPassword = oldPassword;
        this.password = password;
        this.repassword = repassword;
    }

    @NotBlank
    private final String oldPassword;

    @NotBlank
    private final String password;

    @NotBlank
    private final String repassword;


    public String getOldPassword() {
        return oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getRepassword() {
        return repassword;
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
