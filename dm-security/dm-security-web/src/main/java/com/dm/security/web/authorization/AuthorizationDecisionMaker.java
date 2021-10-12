package com.dm.security.web.authorization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

/**
 * 授权决策器
 *
 * @author ldwqh0@outlook.com
 */
public interface AuthorizationDecisionMaker {
    boolean check(Authentication authentication, HttpServletRequest exchange);
}
