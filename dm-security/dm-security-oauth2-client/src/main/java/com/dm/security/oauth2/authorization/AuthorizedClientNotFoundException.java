package com.dm.security.oauth2.authorization;

/**
 * 当尝试获取当前用户在客户端已经保存的AuthorizedClient信息，但没有找到时，发生该异常
 */
public class AuthorizedClientNotFoundException extends Exception {
    private static final long serialVersionUID = -5019028466051778349L;
}
