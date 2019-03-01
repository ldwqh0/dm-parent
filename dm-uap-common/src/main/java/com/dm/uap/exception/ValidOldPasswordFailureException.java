package com.dm.uap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ValidOldPasswordFailureException extends RuntimeException {
	private static final long serialVersionUID = -4328676120309646857L;

	public ValidOldPasswordFailureException(String message) {
		super(message);
	}

	public ValidOldPasswordFailureException() {
		super("原密码校验错误");
	}
}
