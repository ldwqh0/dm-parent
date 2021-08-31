package com.dm.region.controller;

import com.dm.region.converter.RegionConverter;
import com.dm.region.dto.RegionDto;
import com.dm.region.service.RegionService;
import org.apache.commons.lang3.StringUtils;
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

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public List<RegionDto> findAll(@RequestParam(value = "parent", required = false) String parent,
                                   @RequestParam(value = "includeSelf", required = false, defaultValue = "true") Boolean includeSelf) {
        List<RegionDto> regions;
        if (StringUtils.isEmpty(parent)) {
            regions = regionService.findAll();
        } else {
            regions = regionService.findAllChildren(parent);
        }
        if (includeSelf && StringUtils.isNoneBlank(parent)) {
            Optional<RegionDto> self = regionService.findByCode(parent);
            self.ifPresent(regions::add);
        }
        return regions;
    }

    @GetMapping(value = "provinces")
    public List<RegionDto> findProvincial() {
        return regionService.findProvincials();
    }

    @GetMapping(params = {"page", "size"})
    public Page<RegionDto> find(@RequestParam(value = "keyword", required = false) String keyword,
                                @PageableDefault Pageable pageable) {
        return regionService.find(keyword, pageable).map(RegionConverter::toDto);
    }

    @GetMapping(value = "children")
    public List<RegionDto> findChildren(@RequestParam(value = "code") String code) {
        return regionService.findChildren(code);
    }
}
