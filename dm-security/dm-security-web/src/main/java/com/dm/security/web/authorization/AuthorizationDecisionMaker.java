package com.dm.security.web.authorization;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权决策器
 *
 * @author ldwqh0@outlook.com
 */
public interface AuthorizationDecisionMaker {
    boolean check(Authentication authentication, HttpServletRequest exchange);
}
