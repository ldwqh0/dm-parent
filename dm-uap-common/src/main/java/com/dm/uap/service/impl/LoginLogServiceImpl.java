package com.dm.uap.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.LoginLogConverter;
import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.entity.LoginLog;
import com.dm.uap.repository.LoginLogRepository;
import com.dm.uap.service.LoginLogService;

@Service
public class LoginLogServiceImpl implements LoginLogService {

	@Autowired
	private LoginLogRepository loginLogRepository;

	@Autowired
	private LoginLogConverter loginLogConverter;

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

}
