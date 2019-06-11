package com.dm.springboot.autoconfigure.file;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.dm.file.config.FileConfig;
import com.dm.file.entity.FileInfo;

@Configuration
@ConditionalOnClass(FileInfo.class)
@EnableConfigurationProperties(FileConfiguration.class)
@EntityScan(basePackages = "com.dm.file")
@EnableJpaRepositories(basePackages = "com.dm.file")
@ComponentScan(basePackages = "com.dm.file")
public class FileConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "file")
	public FileConfig fileConfig() {
		return new FileConfig();
	}

}
