package com.dm.springboot.autoconfigure.jdbc;

import com.dm.datasource.controller.DmDataSourceController;
import com.dm.datasource.entity.DmDataSource;
import com.dm.datasource.repository.DmDataSourceRepository;
import com.dm.datasource.service.DmDataSourceService;
import com.dm.datasource.service.impl.DmDataSourceServiceImpl;
import com.dm.springboot.autoconfigure.DmEntityScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

@Configuration
@ConditionalOnClass(DmDataSource.class)
@DmEntityScan("com.dm.datasource.entity")
public class MultiDataSourceJpaSupportAutoConfiguration {

    @Bean
    public DmDataSourceRepository dmDataSourceRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(DmDataSourceRepository.class);
    }

    @Bean
    public DmDataSourceController dataSourceController(DmDataSourceRepository dmDataSourceRepository) {
        return new DmDataSourceController(dmDataSourceService(dmDataSourceRepository));
    }


    @Bean
    public DmDataSourceService dmDataSourceService(DmDataSourceRepository dmDataSourceRepository) {
        return new DmDataSourceServiceImpl(dmDataSourceRepository);
    }
}
