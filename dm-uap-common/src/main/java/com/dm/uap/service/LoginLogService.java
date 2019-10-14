package com.dm.uap.service;

import java.util.List;

import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;

public interface LoginLogService {

	public LoginLog save(LoginLogDto log);

	public List<LoginLog> save(List<LoginLogDto> logs);

	public LoginLog save(LoginLog log);
}
