package com.dm.springboot.autoconfigure.common;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dm.common.entity.Audit;
import com.dm.security.core.userdetails.UserDetailsDto;

/**
 * 实体审计相关的配置项目
 * 
 * @author LiDong
 *
 */
@Configuration
@ConditionalOnBean(AuditingHandler.class)
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

    @Configuration
    @ConditionalOnClass(AuditorAware.class)
    @ConditionalOnMissingBean(AuditorAware.class)
    public static class SimpleAuditorAware implements AuditorAware<Audit> {
        @Override
        public Optional<Audit> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetailsDto) {
                    UserDetailsDto ud = (UserDetailsDto) principal;
                    String name = StringUtils.isBlank(ud.getFullname()) ? ud.getUsername() : ud.getFullname();
                    return Optional.ofNullable(Audit.of(ud.getId(), name));
                }
            }
            return Optional.empty();
        }
    }
}
