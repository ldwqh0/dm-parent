package com.dm.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.converter.ResourceConverter;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.service.ResourceService;
import com.dm.collections.Lists;
import com.dm.common.exception.DataNotExistException;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RestController
@RequestMapping({ "resources", "p/resources" })
public class ResourceController {

    private final ResourceService resourceService;

    private final ResourceConverter resourceConverter;

    @Autowired
    public ResourceController(ResourceService resourceService, ResourceConverter resourceConverter) {
        this.resourceService = resourceService;
        this.resourceConverter = resourceConverter;
    }

    @PostMapping
    @ResponseStatus(value = CREATED)
    public ResourceDto save(@RequestBody ResourceDto resource) {
        return resourceConverter.toDto(resourceService.save(resource));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        resourceService.deleteById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(value = CREATED)
    public ResourceDto update(@PathVariable("id") Long id, @RequestBody ResourceDto _resource) {
        return resourceConverter.toDto(resourceService.update(id, _resource));
    }

    @GetMapping("{id}")
    public ResourceDto get(@PathVariable("id") Long id) {
        return resourceService.findById(id).map(resourceConverter::toDto)
                .orElseThrow(DataNotExistException::new);
    }

    @GetMapping(params = { "draw" })
    public Page<ResourceDto> search(@RequestParam("draw") Long draw,
            @PageableDefault Pageable pageable,
            @RequestParam(value = "search", required = false) String keywords) {
        return resourceService.search(keywords, pageable).map(resourceConverter::toDto);
    }

    @GetMapping
    public List<ResourceDto> listAll() {
        return Lists.transform(resourceService.listAll(), resourceConverter::toDto);
    }
}
