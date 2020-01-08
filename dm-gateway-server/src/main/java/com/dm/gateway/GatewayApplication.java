package com.dm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.dm.gateway.controller.GsController;

@SpringBootApplication
@EnableCaching
public class GatewayApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public GsController gsController() {
        return new GsController();
    }
}
