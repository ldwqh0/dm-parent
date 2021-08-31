package com.dm.springboot.autoconfigure.jdbc;

import com.dm.datasource.controller.DmDataSourceController;
import com.dm.datasource.entity.DmDataSource;
import com.dm.datasource.repository.DmDataSourceRepository;
import com.dm.datasource.service.DmDataSourceService;
import com.dm.datasource.service.impl.DmDataSourceServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnClass(DmDataSource.class)
@EntityScan(basePackages = "com.dm.datasource.entity")
@EnableJpaRepositories(basePackages = "com.dm.datasource.repository")
public class MultiDataSourceJpaSupportAutoConfiguration {

    @Bean
    public DmDataSourceController dataSourceController(DmDataSourceRepository dmDataSourceRepository) {
        return new DmDataSourceController(dmDataSourceService(dmDataSourceRepository));
    }


    @Bean
    public DmDataSourceService dmDataSourceService(DmDataSourceRepository dmDataSourceRepository) {
        return new DmDataSourceServiceImpl(dmDataSourceRepository);
    }
}
