package com.dm.wx.oauth2.provider;

import com.dm.wx.oauth2.authentication.WxAuthenticationToken;
import com.dm.wx.oauth2.authentication.WxOauth2AccessTokenAuthenticationToken;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.util.Assert;

import java.util.Collections;

/**
 * 通过微信oauth2登录的适配器
 *
 * @author LiDong
 */
public class WxOauth2AuthenticationProvider implements AuthenticationProvider,
    InitializingBean, MessageSourceAware {

    private final Logger logger = LoggerFactory.getLogger(WxOauth2AuthenticationProvider.class);

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private AuthenticationUserDetailsService<WxOauth2AccessTokenAuthenticationToken> authenticationUserDetailsService;

    private WxOAuth2Service wxOAuth2Service;

    private WxMpUserService wxMpUserService;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private final UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();

    public void setWxMpService(WxMpService wxMpService) {
        this.wxOAuth2Service = wxMpService.getOAuth2Service();
        this.wxMpUserService = wxMpService.getUserService();
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.wxOAuth2Service, "the wxOauth2Service can not be null");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        try {
            final String code = authentication.getCredentials().toString();
            final WxOAuth2AccessToken token = wxOAuth2Service.getAccessToken(code);
            final WxOauth2AccessTokenAuthenticationToken authenticationToken = new WxOauth2AccessTokenAuthenticationToken(token, Collections.singleton(new SimpleGrantedAuthority("ROLE_WX_USER")));
            if (authenticationUserDetailsService != null) {
                UserDetails userDetails = authenticationUserDetailsService.loadUserDetails(authenticationToken);
                preAuthenticationChecks.check(userDetails);
                return new WxAuthenticationToken(token, userDetails, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
            }
            return authenticationToken;
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return WxOAuth2CodeAuthentication.class.isAssignableFrom(authentication);
    }

    public void setAuthenticationUserDetailsService(AuthenticationUserDetailsService<WxOauth2AccessTokenAuthenticationToken> authenticationUserDetailsService) {
        this.authenticationUserDetailsService = authenticationUserDetailsService;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");

                throw new LockedException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.locked",
                    "User account is locked"));
            }

            if (!user.isEnabled()) {
                logger.debug("User account is disabled");

                throw new DisabledException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.disabled",
                    "User is disabled"));
            }

            if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");

                throw new AccountExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.expired",
                    "User account has expired"));
            }
        }
    }
}
