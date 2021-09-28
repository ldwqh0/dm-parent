package com.dm.springboot.autoconfigure.common;

import com.dm.common.entity.Audit;
import com.dm.security.core.userdetails.UserDetailsDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * 实体审计相关的配置项目
 *
 * @author LiDong
 */
@ConditionalOnBean(AuditingHandler.class)
public class AuditingAutoConfiguration {

	private final AuditingHandler handler;

    public AuditingAutoConfiguration(AuditingHandler handler) {
        this.handler = handler;
    }

    /**
	 * 修改AuditingHandler的DateTimeProvider使用，ZonedDateTime获取当前时间<br>
	 * 这个正确的修改方式应该是 在 @EnableJpaAuditing(dateTimeProviderRef =
	 * "zonedDateTimeProvider") 指定<br>
	 * <p>
	 * 参考 {@link EnableJpaAuditing}
	 */
	@PostConstruct
	@Deprecated
	public void configAuditingHandler() {
		handler.setDateTimeProvider(() -> Optional.of(ZonedDateTime.now()));
	}

	@Configuration
	@ConditionalOnClass({ AuditorAware.class, UserDetailsDto.class })
	@ConditionalOnMissingBean(AuditorAware.class)
	public static class SimpleAuditorAware implements AuditorAware<Audit<?, ?>> {
		@Override
		public Optional<Audit<?, ?>> getCurrentAuditor() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				Object principal = authentication.getPrincipal();
				if (principal instanceof UserDetailsDto) {
					UserDetailsDto ud = (UserDetailsDto) principal;
					String name = StringUtils.isBlank(ud.getFullname()) ? ud.getUsername() : ud.getFullname();
					return Optional.of(Audit.of(ud.getId(), name));
				}
			}
			return Optional.empty();
		}
	}
}
