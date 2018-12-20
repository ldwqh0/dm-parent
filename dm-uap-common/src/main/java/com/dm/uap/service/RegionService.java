package com.dm.uap.service;

import java.util.List;

import com.dm.uap.dto.RegionDto;
import com.dm.uap.entity.Region;

public interface RegionService {
	public boolean existAny();

	public List<Region> save(List<RegionDto> regions);

	public Region save(RegionDto region);

	public List<Region> findParentsAndChildren(List<String> region);
}
