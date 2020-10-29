package com.dm.uap.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;

public interface LoginLogService {

    LoginLog save(LoginLogDto log);

    List<LoginLog> save(List<LoginLogDto> logs);

    LoginLog save(LoginLog log);

    Page<LoginLog> list(String query, Pageable pageable);
}
