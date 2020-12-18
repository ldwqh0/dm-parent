package com.dm.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 数据访问权限不足的异常信息
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class DataForbiddenException extends DmRuntimeException {

    private static final long serialVersionUID = 5938425198683333281L;

    public DataForbiddenException(String message) {
        super(message);
    }

    public DataForbiddenException() {
        super("数据访问权限不足");
    }

}
