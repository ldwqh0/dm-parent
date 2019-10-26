package com.dm.uap.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;

public interface LoginLogService {

    public LoginLog save(LoginLogDto log);

    public List<LoginLog> save(List<LoginLogDto> logs);

    public LoginLog save(LoginLog log);

    public Page<LoginLog> list(String query, Pageable pageable);
}
