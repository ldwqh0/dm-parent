package com.dm.auth.controller;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.auth.service.ResourceService;
import com.dm.collections.CollectionUtils;
import com.dm.common.dto.ValidationResult;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.security.authentication.UriResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * 资源管理
 */
@RestController
@RequestMapping({"resources", "p/resources"})
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    /**
     * 保存资源信息
     *
     * @param resource 要保存的资源信息
     * @return 保存后的资源信息
     */
    @PostMapping
    @ResponseStatus(value = CREATED)
    public ResourceDto save(@RequestBody ResourceDto resource) {
        if (exist(resource.getMatcher(), resource.getMatchType(), resource.getMethods(), null)) {
            throw new DataValidateException("指定的资源已经存在了");
        }
        return resourceService.save(resource);
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
     * @param id       要更新的资源的ID
     * @param resource 资源详情
     * @return 更新后的资源详情
     */
    @PutMapping("{id}")
    @ResponseStatus(value = CREATED)
    public ResourceDto update(@PathVariable("id") Long id, @RequestBody ResourceDto resource) {
        if (exist(resource.getMatcher(), resource.getMatchType(), resource.getMethods(), id)) {
            throw new DataValidateException("指定的资源已经存在了");
        }
        return resourceService.update(id, resource);
    }

    /**
     * 根据ID获取资源信息
     *
     * @param id 要获取资源的id
     * @return 获取到的资源信息
     */
    @GetMapping("{id}")
    public ResourceDto get(@PathVariable("id") Long id) {
        return resourceService.findById(id)
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
        return resourceService.search(keyword, pageable);
    }

    /**
     * 获取所有可用资源
     *
     * @return 资源列表
     */
    @Deprecated
    public List<ResourceDto> listAll() {
        return Collections.emptyList();
        // return Lists.transform(resourceService.listAll(), resourceConverter::toDto);
    }

    /**
     * 验证指定资源是否存在
     *
     * @param matcher   匹配路径
     * @param matchType 匹配类型
     * @param methods   匹配的方法
     * @param exclude   要排除的项目
     * @return 返回校验结果
     */
    @RequestMapping("validation")
    public ValidationResult validate(
        @RequestParam("matcher") String matcher,
        @RequestParam("matchType") UriResource.MatchType matchType,
        @RequestParam(value = "methods", required = false) Set<HttpMethod> methods,
        @RequestParam(value = "exclude", required = false) Long exclude) {
        if (exist(matcher, matchType, methods, exclude)) {
            return ValidationResult.failure("指定资源配置已经存在");
        } else {
            return ValidationResult.success();
        }
    }

    private boolean exist(@NotNull String matcher, @NotNull UriResource.MatchType matchType, Collection<HttpMethod> methods, @Nullable Long exclude) {
        if (Objects.isNull(methods)) {
            methods = Collections.emptySet();
        }
        List<AuthResource> resources = resourceService.findByMatcherAnExcludeById(matcher, matchType, exclude);
        for (AuthResource it : resources) {
            Set<HttpMethod> existMethods = it.getMethods();
            if (CollectionUtils.isEmpty(existMethods) && CollectionUtils.isEmpty(methods)) {
                return true;
            }
            if (CollectionUtils.containsAny(existMethods, methods)) {
                return true;
            }
        }
        return false;
    }
}
