package com.dm.springboot.autoconfigure.common;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dm.common.exception.DmRuntimeException;

@Configuration
@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(DmRuntimeException.class)
    @ResponseBody
    public Map<String, Object> dmRuntimeExceptionHandler(DmRuntimeException e, HttpServletRequest request,
            HttpServletResponse response) {
        ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
        if (!Objects.isNull(status)) {
            int code = status.code().value();
            response.setStatus(code);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("path", request.getRequestURI());
            result.put("error", HttpStatus.valueOf(code).getReasonPhrase());
            result.put("message", e.getMessage());
            result.put("status", code);
            result.put("timestamp", ZonedDateTime.now());
            return result;
        } else {
            return null;
        }
    }

    // 增加时间参数反序列化的方法
    @InitBinder
    public void initDateFormatter(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                LocalDate l = null;
                try {
                    l = LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
                } catch (Exception e) {
                }

                if (Objects.isNull(l)) {
                    l = LocalDate.parse(text, DateTimeFormatter.ISO_DATE_TIME);
                } else {

                }
                setValue(l);
            }
        });

        binder.registerCustomEditor(ZonedDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                ZonedDateTime z = null;
                try {
                    z = ZonedDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception e) {
                }
                if (Objects.isNull(z)) {
                    z = ZonedDateTime.parse(text, DateTimeFormatter.ISO_DATE);
                }
                setValue(z);
            }
        });
    }

}
