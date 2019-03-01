package com.dm.uap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DataConflictException extends RuntimeException {
	private static final long serialVersionUID = 6003001106817875856L;

	public DataConflictException(String message) {
		super(message);
	}

	public DataConflictException() {
		super("资源已存在");
	}
}
