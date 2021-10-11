package com.dm.springboot.autoconfigure.common;

import com.dm.common.entity.Audit;
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
    public AuditorAware<Audit<?, ?>> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetailsDto) {
                    UserDetailsDto userdetails = (UserDetailsDto) principal;
                    String name = StringUtils.isBlank(userdetails.getFullname()) ? userdetails.getUsername() : userdetails.getFullname();
                    return Optional.of(Audit.of(userdetails.getId(), name));
                }
            }
            return Optional.empty();
        };
    }
}
