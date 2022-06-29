package com.dm.security.web.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public class AuthenticationObjectMapperFactory {
    private static volatile ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (Objects.isNull(objectMapper)) {
            synchronized (AuthenticationObjectMapperFactory.class) {
                if (Objects.isNull(objectMapper)) {
                    objectMapper = new ObjectMapper();
                    objectMapper.findAndRegisterModules();
                }
            }
        }
        return objectMapper;
    }

}
