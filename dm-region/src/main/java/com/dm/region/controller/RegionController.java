package com.dm.region.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dm.region.converter.RegionConverter;
import com.dm.region.dto.RegionDto;
import com.dm.region.dto.RegionTreeDto;
import com.dm.region.entity.Region;
import com.dm.region.service.RegionService;

/**
 * 区县代码
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("regions")
public class RegionController {

	@Autowired
	private RegionService regionService;

	@Autowired
	private RegionConverter regionConverter;

	@GetMapping(params = "!type")
	public List<RegionDto> findAll() {
		List<Region> regions = regionService.findAll();
		return regions.stream().map(regionConverter::toDto).collect(Collectors.toList());
	}

	@GetMapping(params = "type=tree")
	public List<RegionTreeDto> getTree() {
		List<Region> regions = regionService.findAll();
		return regionConverter.toTreeDto(regions);
	}

	@GetMapping(value = "provinces")
	public List<RegionDto> findProvincial() {
		List<Region> regions = regionService.findProvincials();
		return regions.stream().map(regionConverter::toDto).collect(Collectors.toList());
	}

	@GetMapping(value = "children")
	public List<RegionDto> findChildren(
			@RequestParam(required = true, value = "code") String code) {
		List<Region> regions = regionService.findChildren(code);
		return regions.stream().map(regionConverter::toDto).collect(Collectors.toList());
	}
}
