package com.dm.region.converter;

import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class RegionConverter {

    private RegionConverter() {
    }

    public static List<String> toRegionCodeList(Region region) {
        List<String> result = new ArrayList<>();
        Region current = region;
        while (Objects.nonNull(current)) {
            result.add(0, current.getCode());
            current = current.getParentCode();
        }
        return result;
    }

    public static RegionDto toDto(Region model) {
        return Optional.ofNullable(model).map(input -> {
            RegionDto dto = new RegionDto();
            dto.setCode(model.getCode());
            dto.setName(model.getName());
            Region parentCode = model.getParentCode();
            dto.setLatitude(model.getLatitude());
            dto.setLongitude(model.getLongitude());
            dto.setParent(toDto(parentCode));
            return dto;
        }).orElse(null);
    }
}
