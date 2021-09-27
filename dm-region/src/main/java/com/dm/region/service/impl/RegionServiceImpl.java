package com.dm.region.service.impl;

import com.dm.collections.Lists;
import com.dm.region.converter.RegionConverter;
import com.dm.region.dto.RegionDto;
import com.dm.region.entity.QRegion;
import com.dm.region.entity.Region;
import com.dm.region.repository.RegionRepository;
import com.dm.region.service.RegionService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    private final QRegion qRegion = QRegion.region;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegionDto> findAll() {
        return Lists.transform(regionRepository.findAll(), RegionConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegionDto> findProvincials() {
        return Lists.transform(regionRepository.findAllByParentCode_CodeIsNull(), RegionConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegionDto> findChildren(String code) {
        return Lists.transform(regionRepository.findAllByParentCode_Code(code), RegionConverter::toDto);
    }

    @Override
    @Transactional
    public List<RegionDto> save(List<RegionDto> regions) {
        List<Region> models = Lists.transform(regions, region -> this.copyProperties(new Region(), region));
        return Lists.transform(regionRepository.saveAll(models), RegionConverter::toDto);
    }

    @Override
    public boolean existAny() {
        return regionRepository.count() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegionDto> findAllChildren(String code) {
        return Lists.transform(regionRepository.findAllChildren(code), RegionConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegionDto> findByCode(String parent) {
        return regionRepository.findById(parent).map(RegionConverter::toDto);
    }

    @Override
    public Page<Region> find(String keyword, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (StringUtils.isNoneBlank(keyword)) {
            query.and(qRegion.name.containsIgnoreCase(keyword).or(qRegion.code.containsIgnoreCase(keyword)));
        }
        return regionRepository.findAll(query, pageable);
    }

    @Override
    @Transactional
    public List<Region> saveAll(List<Region> regions) {

        return regionRepository.saveAll(regions);
    }

    @Override
    public boolean exist() {
        return regionRepository.count() > 0;
    }

    @Override
    public Optional<Region> findNextSyncRegion() {
        return regionRepository.findByHrefNotNullAndSyncedIsFalse();
    }


    private Region copyProperties(Region model, RegionDto dto) {
        model.setCode(dto.getCode());
        model.setName(dto.getName());
        model.setLatitude(dto.getLatitude());
        model.setLongitude(dto.getLongitude());
        if (dto.getParent() != null) {
            Region regionParent = new Region();
            copyProperties(regionParent, dto.getParent());
            model.setParentCode(regionParent);
        }
        return model;
    }
}
