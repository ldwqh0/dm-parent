package com.dm.uap.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.RegionConverter;
import com.dm.uap.dto.RegionDto;
import com.dm.uap.entity.Region;
import com.dm.uap.repository.RegionRepository;
import com.dm.uap.service.RegionService;

@Service
public class RegionServiceImpl implements RegionService {

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private RegionConverter regionConverter;

	@Override
	public boolean existAny() {
		return regionRepository.count() > 0;
	}

	@Override
	@Transactional
	public List<Region> save(List<RegionDto> regions) {
		return regions.stream().map(region -> {
			Region region_ = new Region();
			regionConverter.copyProperties(region_, region);
			return region_;
		}).map(regionRepository::save).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Region save(RegionDto region) {
		Region region_ = new Region();
		regionConverter.copyProperties(region_, region);
		return regionRepository.save(region_);
	}

	@Override
	public List<Region> findParentsAndChildren(List<String> regions) {
		if (CollectionUtils.isNotEmpty(regions)) {
			String last = null;
			for (String current : regions) {
				if (StringUtils.isBlank(current)) {
					break;
				} else {
					last = current;
				}
			}
			if (StringUtils.isNoneBlank(last)) {
				Set<Region> result = new LinkedHashSet<>();
				Optional<Region> current = regionRepository.findById(last);
				if (current.isPresent()) {
					result.add(current.get());
					findParents(result, current.get());
					findChildren(result, current.get());
				}
				return result.stream().collect(Collectors.toList());
			}
		}
		return regionRepository.findAll();
	}

	private void findParents(Set<Region> collection, Region current) {
		Region parent = current.getParent();
		if (!Objects.isNull(parent)) {
			collection.add(current.getParent());
			findParents(collection, current.getParent());
		}
	}

	private void findChildren(Set<Region> collection, Region current) {
		List<Region> children = regionRepository.findAllByParentCode(current.getCode());
		if (CollectionUtils.isNotEmpty(children)) {
			collection.addAll(children);
			children.forEach(child -> findChildren(collection, child));
		}
	}
}
