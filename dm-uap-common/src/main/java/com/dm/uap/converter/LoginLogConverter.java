package com.dm.uap.converter;

import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;

@Component
public class LoginLogConverter extends AbstractConverter<LoginLog, LoginLogDto> {

	@Override
	protected LoginLogDto toDtoActual(LoginLog model) {
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

}
