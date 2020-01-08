package com.dm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@SpringBootApplication
@EnableCaching
public class GatewayApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
