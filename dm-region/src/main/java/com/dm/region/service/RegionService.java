package com.dm.region.service;

import java.util.List;

import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;

public interface RegionService {

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	List<Region> findAll();

	/**
	 * 查询省会
	 * 
	 * @return
	 */
	List<Region> findProvincials();

	/**
	 * 查询下级区县
	 * 
	 * @param code
	 * @return
	 */
	List<Region> findChildren(String code);

	/**
	 * 批量保存区划数据
	 */
	List<Region> save(List<RegionDto> regions);

	public boolean existAny();
}