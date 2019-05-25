package com.dm.fileserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.dm.fileserver.config.FileConfig;

@SpringBootApplication
@EnableConfigurationProperties(FileConfig.class)
@EnableJpaAuditing
public class FileServerApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(FileServerApplication.class, args);
	}

}
