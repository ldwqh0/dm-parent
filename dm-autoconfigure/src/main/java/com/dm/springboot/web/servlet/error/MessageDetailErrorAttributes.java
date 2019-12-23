package com.dm.springboot.web.servlet.error;

import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

public class MessageDetailErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> result = super.getErrorAttributes(webRequest, includeStackTrace);
        Throwable t = getError(webRequest);
        if (!Objects.isNull(t)) {
            result.put("message", t.getMessage());
        }
        return result;
    }
}