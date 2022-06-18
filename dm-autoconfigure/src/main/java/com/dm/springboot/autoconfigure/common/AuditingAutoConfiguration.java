package com.dm.springboot.autoconfigure.common;

import com.dm.data.domain.Auditor;
import com.dm.security.core.userdetails.UserDetailsDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 实体审计相关的配置项目
 *
 * @author LiDong
 */
@ConditionalOnBean(AuditingHandler.class)
public class AuditingAutoConfiguration {

    @Bean
    @ConditionalOnClass({AuditorAware.class, UserDetailsDto.class})
    @ConditionalOnMissingBean(AuditorAware.class)
    public AuditorAware<Auditor<?, ?>> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getPrincipal)
            .filter(it -> it instanceof UserDetailsDto)
            .map(it -> (UserDetailsDto) it)
            .map(it -> Auditor.of(it.getId(), StringUtils.getIfBlank(it.getFullName(), it::getUsername)));
    }
}
