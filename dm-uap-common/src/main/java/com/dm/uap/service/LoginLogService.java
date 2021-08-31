package com.dm.uap.service;

import com.dm.uap.dto.LoginLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoginLogService {

    LoginLogDto save(LoginLogDto log);

    List<LoginLogDto> save(List<LoginLogDto> logs);

    Page<LoginLogDto> list(String query, Pageable pageable);
}
