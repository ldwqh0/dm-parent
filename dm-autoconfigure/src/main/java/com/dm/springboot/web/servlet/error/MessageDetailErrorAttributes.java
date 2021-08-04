package com.dm.springboot.web.servlet.error;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * 异常处理类，代码源自{@link DefaultErrorAttributes}这里调整了一下异常信息的产生顺序<br>
 * 因为undertow和tomcat处理异常信息的逻辑不一致，导致前端显示的异常信息也不一致，<br>
 * 这里通过调整解析异常信息的顺序来处理两者的不一致<br>
 *
 * @author ldwqh0@outlook.com
 */
public class MessageDetailErrorAttributes extends DefaultErrorAttributes {

    @Override
    protected String getMessage(WebRequest webRequest, Throwable error) {
        String message = error.getMessage();
        if (StringUtils.isBlank(message)) {
            return super.getMessage(webRequest, error);
        } else {
            return message;
        }
    }
}
