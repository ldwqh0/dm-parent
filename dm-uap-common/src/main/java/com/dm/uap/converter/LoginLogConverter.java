package com.dm.uap.converter;

import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;

public final class LoginLogConverter {

    private LoginLogConverter() {
    }

    public static LoginLogDto toDto(LoginLog model) {
        return LoginLogDto.builder()
            .id(model.getId())
            .loginName(model.getLoginName())
            .ip(model.getIp())
            .type(model.getType())
            .result(model.getResult())
            .time(model.getTime()).build();
    }

}
