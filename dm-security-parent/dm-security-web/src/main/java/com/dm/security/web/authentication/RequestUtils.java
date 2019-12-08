package com.dm.security.web.authentication;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public abstract class RequestUtils {

    public static boolean isJsonRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        return StringUtils.isNotEmpty(accept) && (StringUtils.containsIgnoreCase(accept, "application/json") || StringUtils.containsIgnoreCase(accept, "text/plain"));
    }
}
