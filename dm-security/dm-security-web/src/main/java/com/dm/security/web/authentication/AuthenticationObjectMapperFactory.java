package com.dm.security.web.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public class AuthenticationObjectMapperFactory {
    private static volatile ObjectMapper objectMapper;
    private static final Object lock = new Object();

    public static ObjectMapper getObjectMapper() {
        if (Objects.isNull(objectMapper)) {
            synchronized (lock) {
                if (Objects.isNull(objectMapper)) {
                    objectMapper = new ObjectMapper();
                }
            }
        }
        return objectMapper;
    }

}
