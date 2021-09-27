package com.dm.uap.service.impl;

import com.dm.collections.Lists;
import com.dm.uap.converter.LoginLogConverter;
import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;
import com.dm.uap.entity.QLoginLog;
import com.dm.uap.repository.LoginLogRepository;
import com.dm.uap.service.LoginLogService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Service
public class LoginLogServiceImpl implements LoginLogService {

    private final LoginLogRepository loginLogRepository;


    private final QLoginLog qLoginLog = QLoginLog.loginLog;

    public LoginLogServiceImpl(LoginLogRepository loginLogRepository) {
        this.loginLogRepository = loginLogRepository;
    }


    @Override
    @Transactional
    public LoginLogDto save(LoginLogDto log) {
        return LoginLogConverter.toDto(loginLogRepository.save(copyProperties(new LoginLog(), log)));
    }

    @Override
    @Transactional
    public List<LoginLogDto> save(List<LoginLogDto> logs) {
        return Lists.transform(
            loginLogRepository.saveAll(Lists.transform(logs, it -> copyProperties(new LoginLog(), it))),
            LoginLogConverter::toDto
        );
    }

    @Override
    public Page<LoginLogDto> list(String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (StringUtils.isNotBlank(key)) {
            query.and(qLoginLog.ip.containsIgnoreCase(key)
                .or(qLoginLog.loginName.containsIgnoreCase(key)));
        }
        return loginLogRepository.findAll(query, pageable).map(LoginLogConverter::toDto);
    }


    private LoginLog copyProperties(LoginLog dest, LoginLogDto source) {
        dest.setIp(source.getIp());
        dest.setLoginName(source.getLoginName());
        dest.setResult(source.getResult());
        dest.setTime(source.getTime());
        dest.setType(source.getType());
        return dest;
    }

}
