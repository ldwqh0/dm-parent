package com.dm.region.controller;

import com.dm.collections.Lists;
import com.dm.region.converter.RegionConverter;
import com.dm.region.dto.RegionDto;
import com.dm.region.entity.Region;
import com.dm.region.service.RegionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * 区县代码
 *
 * @author Administrator
 */
@RestController
@RequestMapping("regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionConverter regionConverter;

    @GetMapping
    public List<RegionDto> findAll(@RequestParam(value = "parent", required = false) String parent,
                                   @RequestParam(value = "includeSelf", required = false, defaultValue = "true") Boolean includeSelf) {
        List<Region> regions;
        if (StringUtils.isEmpty(parent)) {
            regions = regionService.findAll();
        } else {
            regions = regionService.findAllChildren(parent);
        }
        if (includeSelf && StringUtils.isNoneBlank(parent)) {
            Optional<Region> self = regionService.findByCode(parent);
            self.ifPresent(regions::add);
        }
        return Lists.transform(regions, regionConverter::toDto);
    }

    @GetMapping(value = "provinces")
    public List<RegionDto> findProvincial() {
        List<Region> regions = regionService.findProvincials();
        return Lists.transform(regions, regionConverter::toDto);
    }

    @GetMapping(params = {"draw"})
    public Page<RegionDto> find(@RequestParam("draw") Long draw,
                                @RequestParam(value = "keywords", required = false) String keywords,
                                @PageableDefault Pageable pageable) {
        return regionService.find(keywords, pageable).map(regionConverter::toDto);
    }

    @GetMapping(value = "children")
    public List<RegionDto> findChildren(@RequestParam(value = "code") String code) {
        List<Region> regions = regionService.findChildren(code);
        return Lists.transform(regions, regionConverter::toDto);
    }
}
