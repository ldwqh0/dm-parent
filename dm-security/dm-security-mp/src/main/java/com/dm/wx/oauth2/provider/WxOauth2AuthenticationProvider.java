package com.dm.wx.oauth2.provider;

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
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import com.dm.wx.oauth2.authentication.WxAuthenticationToken;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

/**
 * 通过微信oauth2登录的适配器
 * 
 * @author LiDong
 *
 */
public class WxOauth2AuthenticationProvider implements AuthenticationProvider,
        InitializingBean, MessageSourceAware {

    private Logger logger = LoggerFactory.getLogger(WxOauth2AuthenticationProvider.class);

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private AuthenticationUserDetailsService<WxMpOAuth2AuthenticationAccessToken> authenticationUserDetailsService;

    private WxMpService wxMpService;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();

    public void setWxMpService(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        try {
            String code = authentication.getCredentials().toString();
            WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(code);
            if (authenticationUserDetailsService != null) {
                UserDetails userDetails = authenticationUserDetailsService
                        .loadUserDetails(new WxMpOAuth2AuthenticationAccessToken(token));

                preAuthenticationChecks.check(userDetails);
                return new WxAuthenticationToken(userDetails, code,
                        authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
            } else {
                return new WxMpOAuth2AuthenticationAccessToken(token);
            }
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return WxOAuth2CodeAuthentication.class.isAssignableFrom(authentication);
    }

    public void setAuthenticationUserDetailsService(
            AuthenticationUserDetailsService<WxMpOAuth2AuthenticationAccessToken> authenticationUserDetailsService) {
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
