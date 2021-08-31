package com.dm.uap.converter;

import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;

import java.util.Optional;

public final class LoginLogConverter {

    private LoginLogConverter() {
    }

    public static LoginLogDto toDto(LoginLog input) {
        return Optional.ofNullable(input).map(model -> {
            LoginLogDto dto = new LoginLogDto();
            dto.setId(model.getId());
            dto.setIp(model.getIp());
            dto.setLoginName(model.getLoginName());
            dto.setResult(model.getResult());
            dto.setTime(model.getTime());
            dto.setType(model.getType());
            return dto;
        }).orElse(null);
    }

}
