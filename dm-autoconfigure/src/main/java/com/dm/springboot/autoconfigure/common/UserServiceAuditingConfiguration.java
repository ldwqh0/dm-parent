package com.dm.springboot.autoconfigure.common;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.entity.User;
import com.dm.uap.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConditionalOnBean(UserService.class)
public class UserServiceAuditingConfiguration {
	@Bean
	@ConditionalOnMissingBean(AuditorAware.class)
	public AuditorAware<User> auditorAware(@Autowired final UserService userService) {
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
