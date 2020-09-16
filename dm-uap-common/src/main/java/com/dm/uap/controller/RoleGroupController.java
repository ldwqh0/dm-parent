package com.dm.uap.controller;

import com.dm.common.exception.DataNotExistException;
import com.dm.uap.converter.RoleGroupConverter;
import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.service.RoleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("roleGroups")
public class RoleGroupController {

    private final RoleGroupService roleGroupService;

    private final RoleGroupConverter roleGroupConverter;

    @Autowired
    public RoleGroupController(RoleGroupService roleGroupService, RoleGroupConverter roleGroupConverter) {
        this.roleGroupService = roleGroupService;
        this.roleGroupConverter = roleGroupConverter;
    }

    @GetMapping
    public Page<RoleGroupDto> listAll(@PageableDefault(size = 1000) Pageable pageable) {
        return roleGroupService.search(null, pageable).map(roleGroupConverter::toDto);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public RoleGroupDto save(@RequestBody RoleGroupDto data) {
        return roleGroupConverter.toDto(roleGroupService.save(data));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        roleGroupService.deleteById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(CREATED)
    public RoleGroupDto update(@PathVariable("id") Long id, @RequestBody RoleGroupDto data) {
        return roleGroupConverter.toDto(roleGroupService.update(id, data));
    }

    @GetMapping("{id}")
    public RoleGroupDto findById(@PathVariable("id") Long id) {
        return roleGroupConverter.toDto(roleGroupService.findById(id).orElseThrow(DataNotExistException::new));
    }
}
