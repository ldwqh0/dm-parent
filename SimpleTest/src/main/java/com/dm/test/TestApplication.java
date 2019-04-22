package com.dm.test;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import com.dm.dingtalk.api.service.DingTalkService;

@SpringBootApplication
@EntityScan
@ComponentScan
@EnableJpaRepositories
@EnableAsync
public class TestApplication {

	@Autowired
	private List<DingTalkService> services;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println(services);
	}
}
