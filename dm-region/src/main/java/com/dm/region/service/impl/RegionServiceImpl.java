package com.dm.region.service.impl;

import com.dm.region.converter.RegionConverter;
import com.dm.region.dto.RegionDto;
import com.dm.region.entity.QRegion;
import com.dm.region.entity.Region;
import com.dm.region.repository.RegionRepository;
import com.dm.region.service.RegionService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    private final RegionConverter regionConverter;

    private final QRegion qRegion = QRegion.region;

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

    @Override
    public Optional<Region> findByCode(String parent) {
        return regionRepository.findById(parent);
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
}
