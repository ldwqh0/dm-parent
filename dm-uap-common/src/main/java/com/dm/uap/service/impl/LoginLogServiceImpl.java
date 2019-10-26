package com.dm.uap.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.LoginLogConverter;
import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;
import com.dm.uap.entity.QLoginLog;
import com.dm.uap.repository.LoginLogRepository;
import com.dm.uap.service.LoginLogService;
import com.querydsl.core.BooleanBuilder;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private LoginLogConverter loginLogConverter;

    private final QLoginLog qLoginLog = QLoginLog.loginLog;

    @Override
    @Transactional
    public LoginLog save(LoginLogDto log) {
        LoginLog log_ = new LoginLog();
        loginLogConverter.copyProperties(log_, log);
        return loginLogRepository.save(log_);
    }

    @Override
    @Transactional
    public List<LoginLog> save(List<LoginLogDto> logs) {
        if (CollectionUtils.isNotEmpty(logs)) {
            List<LoginLog> logs_ = logs.stream().map(log -> {
                LoginLog log_ = new LoginLog();
                loginLogConverter.copyProperties(log_, log);
                return log_;
            }).collect(Collectors.toList());
            return loginLogRepository.saveAll(logs_);
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public LoginLog save(LoginLog log) {
        return loginLogRepository.save(log);
    }

    @Override
    public Page<LoginLog> list(String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (!StringUtils.isNotBlank(key)) {
            query.and(qLoginLog.ip.containsIgnoreCase(key)
                    .or(qLoginLog.loginName.containsIgnoreCase(key)));
        }
        return loginLogRepository.findAll(query, pageable);
    }

}
