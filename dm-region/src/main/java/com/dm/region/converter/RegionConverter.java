package com.dm.region.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;

@Component
public class RegionConverter extends AbstractConverter<Region, RegionDto> {

	@Override
	protected RegionDto toDtoActual(Region model) {
		RegionDto dto = new RegionDto();
		dto.setCode(model.getCode());
		dto.setName(model.getName());
		Region parentCode = model.getParentCode();
		dto.setLatitude(model.getLatitude());
		dto.setLongitude(model.getLongitude());
		if (!Objects.isNull(parentCode)) {
			dto.setParent(this.toDto(parentCode));
		}
		return dto;
	}

	@Override
	public Region copyProperties(Region model, RegionDto dto) {
		model.setCode(dto.getCode());
		model.setName(dto.getName());
		model.setLatitude(dto.getLatitude());
		model.setLongitude(dto.getLongitude());
		if (dto.getParent() != null) {
			Region regionParent = new Region();
			this.copyProperties(regionParent, dto.getParent());
			model.setParentCode(regionParent);
		}
		return model;
	}

	public List<String> toRegionCodeList(Region region) {
		List<String> result = new ArrayList<String>();
		Region current = region;
		while (!Objects.isNull(current)) {
			result.add(0, current.getCode());
			current = current.getParentCode();
		}
		return result;
	}
}
