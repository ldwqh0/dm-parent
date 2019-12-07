package com.dm.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 表示文件的请求范围异常
 * 
 * @author ldwqh0@outlook.com
 *
 * @since 0.2.3
 *
 */
@ResponseStatus(code = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
public class RangeNotSatisfiableException extends Exception {

    private static final long serialVersionUID = 6131299317985347609L;

    public RangeNotSatisfiableException() {
        super("Range Not Satisfiable");
    }

    public RangeNotSatisfiableException(String message) {
        super(message);
    }

    public RangeNotSatisfiableException(String message, Throwable e) {
        super(message, e);
    }
}
