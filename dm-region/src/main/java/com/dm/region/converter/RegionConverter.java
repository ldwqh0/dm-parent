package com.dm.region.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.region.dto.RegionDto;
import com.dm.region.dto.RegionTreeDto;
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
			dto.setParentCode(this.toDto(parentCode));
		}
		return dto;
	}

	@Override
	public void copyProperties(Region model, RegionDto dto) {
		model.setCode(dto.getCode());
		model.setName(dto.getName());
		model.setLatitude(dto.getLatitude());
		model.setLongitude(dto.getLongitude());
		if (dto.getParentCode() != null) {
			Region regionParent = new Region();
			this.copyProperties(regionParent, dto.getParentCode());
			model.setParentCode(regionParent);
		}
	}

	public List<RegionTreeDto> toTreeDto(List<Region> regions) {
		Map<String, RegionTreeDto> regionTreeMap = new HashMap<>();
		regions.forEach(region -> {
			RegionTreeDto treeDto = this.toRegionTreeDto(region);
			regionTreeMap.put(region.getCode(), treeDto);
		});
		Collection<RegionTreeDto> regionTrees = regionTreeMap.values();
		regionTrees.forEach(region -> {
			String parentCode = region.getParentCode();
			if (StringUtils.isNotBlank(parentCode)) {
				regionTreeMap.get(parentCode).addChildren(region);
			}
		});
		List<RegionTreeDto> result = new ArrayList<RegionTreeDto>(regionTrees);
		result.sort((v1, v2) -> {
			return Integer.valueOf(v1.getCode()) - Integer.valueOf(v2.getCode());
		});
		return result.stream().filter(item -> StringUtils.isEmpty(item.getParentCode())).collect(Collectors.toList());
	}

	private RegionTreeDto toRegionTreeDto(Region region) {
		RegionTreeDto treeDto = new RegionTreeDto();
		treeDto.setCode(region.getCode());
		treeDto.setLatitude(region.getLatitude());
		treeDto.setLongitude(region.getLongitude());
		treeDto.setName(region.getName());
		Region parent = region.getParentCode();
		if (!Objects.isNull(parent)) {
			treeDto.setParentCode(parent.getCode());
		}
		return treeDto;
	}
}
