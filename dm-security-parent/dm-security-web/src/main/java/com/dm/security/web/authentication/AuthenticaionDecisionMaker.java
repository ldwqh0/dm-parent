package com.dm.security.web.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

/**
 * 授权决策器
 * 
 * @author ldwqh0@outlook.com
 *
 */
public interface AuthenticaionDecisionMaker {
    public boolean check(Authentication authentication, HttpServletRequest exchange);
}
