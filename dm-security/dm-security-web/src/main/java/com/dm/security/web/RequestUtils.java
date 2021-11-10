package com.dm.security.web;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestUtils {

    /**
     * 判断一个请求是否要求JSON的响应
     *
     * @param request
     * @return
     */
    public static boolean isJsonRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        return StringUtils.isNotEmpty(accept) && (StringUtils.containsIgnoreCase(accept, "application/json")
            || StringUtils.containsIgnoreCase(accept, "text/plain"));
    }
}
