package com.dm.springboot.autoconfigure.file;

import com.dm.file.config.FileConfig;
import com.dm.file.controller.FileController;
import com.dm.file.entity.FileInfo;
import com.dm.file.listener.FileListener;
import com.dm.file.repository.FileInfoRepository;
import com.dm.file.service.FileInfoService;
import com.dm.file.service.FileStorageService;
import com.dm.file.service.PackageFileService;
import com.dm.file.service.ThumbnailService;
import com.dm.file.service.impl.DefaultThumbnailServiceImpl;
import com.dm.file.service.impl.FileServiceImpl;
import com.dm.file.service.impl.LocalFileStorageServiceImpl;
import com.dm.file.service.impl.MapPackageFileServiceImpl;
import com.dm.springboot.autoconfigure.DmEntityScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

@Configuration
@ConditionalOnClass(FileInfo.class)
@DmEntityScan("com.dm.file")
public class FileConfiguration {

    @Bean
    public FileInfoRepository fileInfoRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(FileInfoRepository.class);
    }


    @Bean
    public FileInfoService fileInfoService(@Autowired FileInfoRepository fileInfoRepository) {
        return new FileServiceImpl(
            fileStorageService(),
            fileInfoRepository
        );
    }

    @Bean
    public FileController fileController(@Autowired FileInfoRepository fileInfoRepository) {
        return new FileController(
            fileInfoService(fileInfoRepository),
            thumbnailService(),
            fileConfig(),
            fileStorageService(),
            packageFileService()
        );
    }


    @Bean
    @ConfigurationProperties(prefix = "dm.fileserver")
    public FileConfig fileConfig() {
        return new FileConfig();
    }


    @Bean
    public FileListener fileListener() {
        FileListener fileListener = new FileListener();
        fileListener.setStorageService(fileStorageService());
        return fileListener;
    }

    @Bean
    @ConditionalOnMissingBean(FileStorageService.class)
    public FileStorageService fileStorageService() {
        return new LocalFileStorageServiceImpl(fileConfig());
    }

    @Bean
    @ConditionalOnMissingBean(ThumbnailService.class)
    public ThumbnailService thumbnailService() {
        return new DefaultThumbnailServiceImpl(fileStorageService());
    }

    @Bean
    @ConditionalOnMissingBean(PackageFileService.class)
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public PackageFileService packageFileService() {
        return new MapPackageFileServiceImpl();
    }
}
