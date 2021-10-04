package com.dm.wx.oauth2.authentication;

import org.springframework.security.core.AuthenticationException;

public class StateValidationFailureException extends AuthenticationException {
    public StateValidationFailureException(String msg) {
        super(msg);
    }
}
