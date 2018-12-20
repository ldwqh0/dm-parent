package com.dm.uap.converter;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.RegionDto;
import com.dm.uap.entity.Region;

@Component
public class RegionConverter extends AbstractConverter<Region, RegionDto> {

	@Override
	protected RegionDto toDtoActual(Region model) {
		RegionDto region = new RegionDto();
		region.setName(model.getName());
		region.setCode(model.getCode());
		Region _parent = model.getParent();
		if (!Objects.isNull(_parent)) {
			region.setParent(toDto(_parent));
		}
		return region;
	}

	@Override
	public void copyProperties(Region model, RegionDto dto) {
		model.setCode(dto.getCode());
		model.setName(dto.getName());
		RegionDto _parent = dto.getParent();
		if (!Objects.isNull(_parent)) {
			Region parent = new Region();
			parent.setCode(_parent.getCode());
			model.setParent(parent);
		}
	}

}
