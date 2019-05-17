package com.dm.region.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.region.converter.RegionConverter;
import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;
import com.dm.region.repository.RegionRepository;
import com.dm.region.service.RegionService;

@Service
public class RegionServiceImpl implements RegionService {

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private RegionConverter regionConverter;

	@Override
	public List<Region> findAll() {
		return regionRepository.findAll();
	}

	@Override
	public List<Region> findProvincials() {
		return regionRepository.findAllByParentCode_CodeIsNull();
	}

	@Override
	public List<Region> findChildren(String code) {
		return regionRepository.findAllByParentCode_Code(code);
	}

	@Override
	@Transactional
	public List<Region> save(List<RegionDto> regions) {
		List<Region> re = regions.stream().map(region -> {
			Region model = new Region();
			regionConverter.copyProperties(model, region);
			return model;
		}).collect(Collectors.toList());
		return regionRepository.saveAll(re);
	}

	@Override
	public boolean existAny() {
		return regionRepository.count() > 0;
	}

	@Override
	public List<Region> findAllChildren(String code) {
		return regionRepository.findAllChildren(code);
	}
}
