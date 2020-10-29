package com.dm.springboot.web.reactive.error;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

public class MessageDetailErrorAttributes extends DefaultErrorAttributes {

    public MessageDetailErrorAttributes(boolean includeException) {
        super(includeException);
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
        Throwable error = getError(request);
        errorAttributes.put("message", determineMessage(error));
        return errorAttributes;
    }

    private String determineMessage(Throwable error) {
        if (Objects.nonNull(error)) {
            if (error instanceof ResponseStatusException) {
                return ((ResponseStatusException) error).getReason();
            }
            return error.getMessage();
        } else {
            return "no more message";
        }

    }
}
