package com.dm.springboot.autoconfigure.region;

import com.dm.collections.CollectionUtils;
import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;
import com.dm.region.service.RegionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnClass(Region.class)
@Import(RegionBeanDefineConfiguration.class)
public class RegionConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RegionConfiguration.class);

    private final ObjectMapper objectMapper;

    private final RegionService regionService;

    public RegionConfiguration(ObjectMapper objectMapper, RegionService regionService) {
        this.objectMapper = objectMapper;
        this.regionService = regionService;
    }

    /**
     * 初始化区县数据
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
