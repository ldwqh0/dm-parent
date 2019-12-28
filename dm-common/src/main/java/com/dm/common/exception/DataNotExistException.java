package com.dm.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DataNotExistException extends RuntimeException {

    private static final long serialVersionUID = 8462512150406664119L;

    public DataNotExistException(String message) {
        super(message);
    }

    public DataNotExistException() {
        super("资源不存在");
    }

}
