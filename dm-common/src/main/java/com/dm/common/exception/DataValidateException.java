package com.dm.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "资源校验错误")
public class DataValidateException extends RuntimeException {

	private static final long serialVersionUID = 5938425198683333281L;

	public DataValidateException(String msg) {
		super(msg);
	}

}
