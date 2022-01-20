package com.dm.region.converter;

import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RegionConverter {

    private RegionConverter() {
    }

    public static List<String> toRegionCodeList(Region region) {
        List<String> result = new ArrayList<>();
        Region current = region;
        while (Objects.nonNull(current)) {
            result.add(0, current.getCode());
            current = current.getParent().orElse(null);
        }
        return result;
    }

    public static RegionDto toDto(Region model) {
        return new RegionDto(
            model.getCode(),
            model.getName(),
            model.getLongitude(),
            model.getLatitude(),
            model.getParent().map(RegionConverter::toSimpleDto).orElse(null)
        );
    }

    private static RegionDto toSimpleDto(Region model) {
        return Objects.isNull(model) ? null : new RegionDto(
            model.getCode(),
            model.getName(),
            model.getLongitude(),
            model.getLatitude(),
            null
        );
    }
}
