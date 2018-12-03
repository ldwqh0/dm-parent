package com.dm.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class DmZuulApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(DmZuulApplication.class, args);
	}

}
