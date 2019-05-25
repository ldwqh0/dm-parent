package com.dm.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class DmRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8806453411801350604L;

	public DmRuntimeException() {
		super();
	}

	public DmRuntimeException(String message) {
		super(message);
	}

	public DmRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DmRuntimeException(Throwable cause) {
		super(cause);
	}

	protected DmRuntimeException(String message, Throwable cause,
			boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
