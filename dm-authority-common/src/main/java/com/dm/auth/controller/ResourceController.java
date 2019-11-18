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
import com.dm.auth.entity.Resource;
import com.dm.auth.service.ResourceService;
import com.dm.common.exception.DataNotExistException;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({ "resources", "p/resources" })
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceConverter resourceConverter;

    @PostMapping
    @ResponseStatus(value = CREATED)
    public ResourceDto save(@RequestBody ResourceDto resource) {
        return resourceConverter.toDto(resourceService.save(resource)).get();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        resourceService.deleteById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(value = CREATED)
    public ResourceDto update(@PathVariable("id") Long id, @RequestBody ResourceDto _resource) {
        return resourceConverter.toDto(resourceService.update(id, _resource)).get();
    }

    @GetMapping("{id}")
    public ResourceDto get(@PathVariable("id") Long id) {
        return resourceConverter.toDto(resourceService.findById(id)).orElseThrow(DataNotExistException::new);
    }

    @GetMapping(params = { "draw" })
    public Page<ResourceDto> search(@RequestParam("draw") Long draw,
            @PageableDefault Pageable pageable,
            @RequestParam(value = "search", required = false) String keywords) {
        Page<Resource> resources = resourceService.search(keywords, pageable);
        return resources.map(resourceConverter::toDto).map(Optional::get);
    }

    @GetMapping
    public List<ResourceDto> listAll() {
        List<Resource> resources = resourceService.listAll();
        return resourceConverter.toDto(resources);
    }
}
