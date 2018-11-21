package com.dm.springboot.autoconfigure.common;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 实体审计相关的配置项目
 * 
 * @author LiDong
 *
 */
@Configuration
@EnableJpaAuditing
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
		handler.setDateTimeProvider(new DateTimeProvider() {
			@Override
			public Optional<TemporalAccessor> getNow() {
				return Optional.of(ZonedDateTime.now());
			}
		});
	}
}
