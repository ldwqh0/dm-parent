package com.dm.springboot.autoconfigure.file;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.dm.file.config.FileConfig;
import com.dm.file.controller.FileController;
import com.dm.file.converter.FileInfoConverter;
import com.dm.file.entity.FileInfo;
import com.dm.file.service.FileInfoService;
import com.dm.file.service.FileStorageService;
import com.dm.file.service.ThumbnailService;
import com.dm.file.service.impl.FileServiceImpl;
import com.dm.file.service.impl.LocalFileStorageServiceImpl;
import com.dm.file.service.impl.LocalThumbnailServiceImpl;

@Configuration
@ConditionalOnClass(FileInfo.class)
@EnableConfigurationProperties(FileConfiguration.class)
@EntityScan(basePackages = "com.dm.file")
@EnableJpaRepositories(basePackages = "com.dm.file")
public class FileConfiguration {

    @Bean
    public FileInfoService FileInfoService() {
        return new FileServiceImpl();
    }

    @Bean
    public FileController fileController() {
        return new FileController();
    }

    @Bean
    public FileInfoConverter fileInfoConverter() {
        return new FileInfoConverter();
    }

    @Bean
    @ConfigurationProperties(prefix = "file")
    public FileConfig fileConfig() {
        return new FileConfig();
    }

    @Bean
    @ConditionalOnMissingBean(FileStorageService.class)
    public FileStorageService fileStorageService() {
        return new LocalFileStorageServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ThumbnailService.class)
    public ThumbnailService thumbnailService() {
        return new LocalThumbnailServiceImpl();
    }

}
