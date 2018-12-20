package com.dm.uap.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.converter.RegionConverter;
import com.dm.uap.dto.RegionDto;
import com.dm.uap.entity.Region;
import com.dm.uap.service.RegionService;

@RestController
@RequestMapping("regions")
public class RegionController {

	@Autowired
	private RegionService regionService;
	
	@Autowired
	private RegionConverter regionConverter;

	@GetMapping
	public List<RegionDto> findAll(@CurrentUser UserDetailsDto user) {
		List<Region> regions = regionService.findParentsAndChildren(user.getRegion());
		return regions.stream().map(regionConverter::toDto).collect(Collectors.toList());
	}
}
