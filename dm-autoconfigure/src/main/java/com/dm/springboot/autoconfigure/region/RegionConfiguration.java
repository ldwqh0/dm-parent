package com.dm.springboot.autoconfigure.region;

import com.dm.collections.CollectionUtils;
import com.dm.collections.Lists;
import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;
import com.dm.region.service.RegionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConditionalOnClass(Region.class)
@Import(RegionBeanDefineConfiguration.class)
public class RegionConfiguration implements InitializingBean {

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
    public void afterPropertiesSet() {
        if (!regionService.exist()) {
            try (InputStream iStream = this.getClass().getClassLoader().getResourceAsStream("regions.json")) {
                MapType elementType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
                CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elementType);
                List<Map<String, Object>> result = objectMapper.readValue(iStream, collectionType);
                if (CollectionUtils.isNotEmpty(result)) {
                    List<RegionDto> regions = Lists.transform(result, it -> {
                        String parentCode = (String) it.get("parent");
                        RegionDto parent = StringUtils.isBlank(parentCode) ? null : new RegionDto(parentCode);
                        return new RegionDto(
                            (String) it.get("code"),
                            (String) it.get("name"),
                            Double.valueOf(String.valueOf(it.get("lat"))),
                            Double.valueOf(String.valueOf(it.get("lng"))),
                            parent
                        );
                    });
                    regionService.save(regions);
                }
            } catch (IOException e) {
                log.error("解析json文件时发生错误", e);
            }
        }
    }


}
