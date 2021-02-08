package com.dm.uap.service;

import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoginLogService {

    LoginLog save(LoginLogDto log);

    List<LoginLog> save(List<LoginLogDto> logs);

    LoginLog save(LoginLog log);

    Page<LoginLog> list(String query, Pageable pageable);
}
