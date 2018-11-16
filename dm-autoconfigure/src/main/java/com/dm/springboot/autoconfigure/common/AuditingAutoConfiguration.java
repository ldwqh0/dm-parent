package com.dm.springboot.autoconfigure.common;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.entity.User;
import com.dm.uap.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 实体审计相关的配置项目
 * 
 * @author LiDong
 *
 */
@Configuration
@EnableJpaAuditing
@Slf4j
@ConditionalOnBean(AuditingHandler.class)
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

	@Bean
	@ConditionalOnBean(UserService.class)
	public AuditorAware<User> auditorAware(@Autowired UserService userService) {
		log.info("create default auditorAware");
		return new AuditorAware<User>() {
			@Override
			public Optional<User> getCurrentAuditor() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication != null) {
					Object principal = authentication.getPrincipal();
					if (principal instanceof UserDetailsDto) {
						UserDetailsDto ud = (UserDetailsDto) principal;
						return userService.get(ud.getId());
					}
				}
				return Optional.empty();
			}
		};
	}
}
