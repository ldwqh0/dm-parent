package com.dm.region.service;

import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RegionService {

    /**
     * 查询所有
     */
    List<RegionDto> findAll();

    /**
     * 查询省会
     */
    List<RegionDto> findProvincials();

    /**
     * 查询下级区县
     *
     * @param code
     */
    List<RegionDto> findChildren(String code);

    /**
     * 批量保存区划数据
     */
    List<RegionDto> save(List<RegionDto> regions);

    /**
     * 获取指定节点的所有子代
     *
     * @param code 要查询的父代
     * @return 所有子代
     */
    List<RegionDto> findAllChildren(String code);

    Optional<RegionDto> findByCode(String parent);

    Page<Region> find(String keyword, Pageable pageable);

    List<Region> saveAll(List<Region> regions);

    boolean exist();

    Optional<Region> findNextSyncRegion();
}
