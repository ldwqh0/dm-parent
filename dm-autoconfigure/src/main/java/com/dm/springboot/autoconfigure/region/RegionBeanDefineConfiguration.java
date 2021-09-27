package com.dm.springboot.autoconfigure.region;

import com.dm.region.controller.RegionController;
import com.dm.region.repository.RegionRepository;
import com.dm.region.service.RegionService;
import com.dm.region.service.impl.RegionServiceImpl;
import com.dm.springboot.autoconfigure.DmEntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

@DmEntityScan({"com.dm.region"})
public class RegionBeanDefineConfiguration {
    @Bean
    public RegionRepository regionRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(RegionRepository.class);
    }

    @Bean
    public RegionService regionService(RegionRepository regionRepository) {
        return new RegionServiceImpl(regionRepository);
    }

    @Bean
    public RegionController regionController(RegionService regionService) {
        return new RegionController(regionService);
    }
}
