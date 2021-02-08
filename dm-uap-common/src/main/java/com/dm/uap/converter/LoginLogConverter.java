package com.dm.uap.converter;

import com.dm.common.converter.Converter;
import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginLogConverter implements Converter<LoginLog, LoginLogDto> {

    private LoginLogDto toDtoActual(LoginLog model) {
        LoginLogDto dto = new LoginLogDto();
        dto.setId(model.getId());
        dto.setIp(model.getIp());
        dto.setLoginName(model.getLoginName());
        dto.setResult(model.getResult());
        dto.setTime(model.getTime());
        dto.setType(model.getType());
        return dto;
    }

    @Override
    public LoginLog copyProperties(LoginLog dest, LoginLogDto source) {
        dest.setIp(source.getIp());
        dest.setLoginName(source.getLoginName());
        dest.setResult(source.getResult());
        dest.setTime(source.getTime());
        dest.setType(source.getType());
        return dest;
    }

    @Override
    public LoginLogDto toDto(LoginLog model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
