package com.dm.security.web;

import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Objects;

public abstract class RequestUtils {
    private static volatile MediaTypeRequestMatcher jsonRequestMatcher;

    /**
     * 判断一个请求是否要求JSON的响应
     *
     * @param request 要严重的请求
     * @return 验证是否通过
     */
    public static boolean isJsonRequest(HttpServletRequest request) {
        return getRequestMatcher().matches(request);
    }

    private static RequestMatcher getRequestMatcher() {
        if (Objects.isNull(jsonRequestMatcher)) {
            synchronized (RequestUtils.class) {
                if (Objects.isNull(jsonRequestMatcher)) {
                    jsonRequestMatcher = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
                    jsonRequestMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
                }
            }
        }
        return jsonRequestMatcher;
    }
}
