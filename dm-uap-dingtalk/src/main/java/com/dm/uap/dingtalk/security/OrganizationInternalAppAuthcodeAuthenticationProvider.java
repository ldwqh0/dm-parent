package com.dm.uap.dingtalk.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.dm.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dm.dingtalk.api.service.DingTalkService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrganizationInternalAppAuthcodeAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private AuthenticationUserDetailsService<DingTalkAuthCodeAuthenticationToken> userDetailsService;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private String corpid;

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    @Autowired
    private List<DingTalkService> dingTalkServices;

    public void setDingTalkService(List<DingTalkService> dingTalkServices) {
        this.dingTalkServices = dingTalkServices;
    }

    public void setUserDetailsService(
            AuthenticationUserDetailsService<DingTalkAuthCodeAuthenticationToken> userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(DingTalkAuthCodeAuthenticationToken.class, authentication,
                "DingTalkAuthcodeAuthenticationProvider only support DingTalkAuthCodeAuthenticationToken");
        // 首先通过钉钉API获取钉钉用户信息
        DingTalkAuthCodeAuthenticationToken token = (DingTalkAuthCodeAuthenticationToken) authentication;
        OapiUserGetuserinfoResponse rsp = null;
        for (DingTalkService dingTalkService : dingTalkServices) {
            if (Objects.isNull(rsp)) {
                try {
                    rsp = dingTalkService.getUserByAuthCode(corpid, String.valueOf(token.getPrincipal()));
                } catch (Exception e) {
                    log.info("get user info with code err , the code is [" + token.getPrincipal() + "]");
                }
            } else {
                break;
            }
        }
        if (Objects.isNull(rsp)) {
            throw new BadCredentialsException("can not find user with auth code [" + token.getPrincipal() + "]");
        } else {
            token = new DingTalkAuthCodeAuthenticationToken(rsp.getUserid());
        }
        // 在通过本地方法获取用户在系统中的信息
        UserDetails user = userDetailsService.loadUserDetails(token);
        Collection<? extends GrantedAuthority> authorities = authoritiesMapper.mapAuthorities(user.getAuthorities());
        // 创建一个成功的认证
        return new DingTalkAuthCodeAuthenticationToken(user, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DingTalkAuthCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(corpid, "the corpid can not be null");
        Assert.notNull(userDetailsService, "the userDetailsServices can not be null");
        Assert.notEmpty(dingTalkServices, "the dingTalkServices can not be empty");
    }
}
