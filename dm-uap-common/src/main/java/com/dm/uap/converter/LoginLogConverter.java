package com.dm.uap.converter;

import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;

public final class LoginLogConverter {

    private LoginLogConverter() {
    }

    public static LoginLogDto toDto(LoginLog model) {
        return new LoginLogDto(
            model.getId(),
            model.getLoginName(),
            model.getIp(),
            model.getType(),
            model.getResult(),
            model.getTime()
        );
    }

}
