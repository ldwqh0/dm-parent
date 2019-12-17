package com.dm.springboot.autoconfigure.common;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.dm.common.exception.DmRuntimeException;

@Configuration
@ControllerAdvice
@ConditionalOnClass(DmRuntimeException.class)
public class WebControllerAdvice {

    @ExceptionHandler(DmRuntimeException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> dmRuntimeExceptionHandler(DmRuntimeException e, WebRequest request) {
        ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
        if (!Objects.isNull(status)) {
            int code = status.code().value();
            Map<String, Object> result = new HashMap<String, Object>();
            // TODO 请求路径如何获取？
            // result.put("path", request.getRequestURI());
            result.put("error", HttpStatus.valueOf(code).getReasonPhrase());
            result.put("message", e.getMessage());
            result.put("status", code);
            result.put("timestamp", ZonedDateTime.now());
            return ResponseEntity.status(status.code()).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
