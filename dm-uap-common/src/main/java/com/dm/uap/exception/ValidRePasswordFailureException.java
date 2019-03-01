package com.dm.uap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ValidRePasswordFailureException extends RuntimeException {
	private static final long serialVersionUID = -4328676120309646857L;

	public ValidRePasswordFailureException(String message) {
		super(message);
	}

	public ValidRePasswordFailureException() {
		super("两次密码输入不一致");
	}
}
