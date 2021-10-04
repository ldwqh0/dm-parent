package com.dm.wx.oauth2.authentication;

import org.springframework.security.core.AuthenticationException;

public class CodeNotFoundException extends AuthenticationException {
    public CodeNotFoundException(String message) {
        super((message));
    }
}
