package com.dm.springboot.autoconfigure.region;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.dm.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;
import com.dm.region.service.RegionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import lombok.extern.slf4j.Slf4j;

@EntityScan({ "com.dm.region" })
@EnableJpaRepositories({ "com.dm.region" })
@ComponentScan({ "com.dm.region" })
@Configuration
@ConditionalOnClass(Region.class)
@Slf4j
public class RegionConfiguration {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegionService regionService;

    /**
     * 初始化区县数据
     * 
     * @throws Exception
     */
    @PostConstruct
    public void initRegion() {
        if (!regionService.existAny()) {
            try (InputStream iStream = this.getClass().getClassLoader().getResourceAsStream("regions.json")) {
                MapType elementType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class,
                        String.class);
                CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,
                        elementType);
                List<Map<String, Object>> result = objectMapper.readValue(iStream, collectionType);
                if (CollectionUtils.isNotEmpty(result)) {
                    List<RegionDto> regions = result.stream().map(r -> {
                        RegionDto region = new RegionDto();
                        region.setName((String) r.get("name"));
                        region.setCode((String) r.get("code"));
                        region.setLatitude(Double.valueOf(String.valueOf(r.get("lat"))));
                        region.setLongitude(Double.valueOf(String.valueOf(r.get("lng"))));
                        String parentCode = (String) r.get("parent");
                        if (StringUtils.isNotBlank(parentCode)) {
                            RegionDto parent = new RegionDto();
                            parent.setCode(parentCode);
                            region.setParent(parent);
                        }
//                        if (StringUtils.isBlank(region.getCode())) {
//                            System.out.println(region);
//                        }
                        return region;
                    }).collect(Collectors.toList());
                    regionService.save(regions);
                }
            } catch (IOException e) {
                log.error("解析json文件时发生错误", e);
            }
        }
    }
}
