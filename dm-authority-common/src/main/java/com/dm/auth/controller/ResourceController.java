package com.dm.auth.controller;

import com.dm.auth.converter.ResourceConverter;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.service.ResourceService;
import com.dm.collections.Lists;
import com.dm.common.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * 资源管理
 */
@RestController
@RequestMapping({"resources", "p/resources"})
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    private final ResourceConverter resourceConverter;

    /**
     * 保存资源信息
     *
     * @param resource
     * @return
     */
    @PostMapping
    @ResponseStatus(value = CREATED)
    public ResourceDto save(@RequestBody ResourceDto resource) {
        return resourceConverter.toDto(resourceService.save(resource));
    }

    /**
     * 删除一条资源记录
     *
     * @param id 要删除的资源的ID
     */
    @DeleteMapping("{id}")
    @ResponseStatus(value = NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        resourceService.deleteById(id);
    }

    /**
     * 更新一个资源
     *
     * @param id        要更新的资源的ID
     * @param _resource 资源详情
     * @return 更新后的资源详情
     */
    @PutMapping("{id}")
    @ResponseStatus(value = CREATED)
    public ResourceDto update(@PathVariable("id") Long id, @RequestBody ResourceDto _resource) {
        return resourceConverter.toDto(resourceService.update(id, _resource));
    }

    /**
     * 根据ID获取资源信息
     *
     * @param id 要获取资源的id
     * @return 获取到的资源信息
     */
    @GetMapping("{id}")
    public ResourceDto get(@PathVariable("id") Long id) {
        return resourceService.findById(id).map(resourceConverter::toDto)
            .orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取资源列表
     *
     * @param pageable 分页信息
     * @param keyword  搜索关键字
     * @return 搜索结果
     */
    @GetMapping(params = {"draw"})
    public Page<ResourceDto> search(
        @PageableDefault Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword) {
        return resourceService.search(keyword, pageable).map(resourceConverter::toDto);
    }

    /**
     * 获取所有可用资源
     *
     * @return 资源列表
     */
    @GetMapping
    public List<ResourceDto> listAll() {
        return Lists.transform(resourceService.listAll(), resourceConverter::toDto);
    }
}
