package com.dm.region.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    boolean existAny();

    /**
     * 获取指定节点的所有子代
     * 
     * @param parentCode
     * @return
     */
    List<Region> findAllChildren(String code);

    Optional<Region> findByCode(String parent);

    Page<Region> find(String keywords, Pageable pageable);
}
