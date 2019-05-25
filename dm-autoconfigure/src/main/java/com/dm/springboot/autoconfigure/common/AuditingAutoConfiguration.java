package com.dm.springboot.autoconfigure.common;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dm.common.exception.DataValidateException;

/**
 * 实体审计相关的配置项目
 * 
 * @author LiDong
 *
 */
@Configuration
@ConditionalOnBean(AuditingHandler.class)
@Import(UserServiceAuditingConfiguration.class)
public class AuditingAutoConfiguration {

	@Autowired
	private AuditingHandler handler;

	/**
	 * 修改AuditingHandler的DateTimeProvider使用，ZonedDateTime获取当前时间
	 */
	@PostConstruct
	public void configAuditingHandler() {
		handler.setDateTimeProvider(() -> Optional.of(ZonedDateTime.now()));
	}

}
