package com.dm.dingtalk.api.exception;

public class DUserNotFoundException extends Exception {
	private static final long serialVersionUID = -2543826650069165995L;

	public DUserNotFoundException() {
		super("can not find user with given id");
	}

	public DUserNotFoundException(String msg) {
		super(msg);
	}

	public DUserNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

}
