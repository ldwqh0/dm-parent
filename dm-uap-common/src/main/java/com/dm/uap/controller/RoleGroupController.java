package com.dm.uap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.common.exception.DataNotExistException;
import com.dm.uap.converter.RoleGroupConverter;
import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.service.RoleGroupService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("roleGroups")
public class RoleGroupController {

    @Autowired
    private RoleGroupService roleGroupService;

    @Autowired
    private RoleGroupConverter roleGroupConverter;

    @GetMapping
    public List<RoleGroupDto> listAll(@PageableDefault(size = 1000) Pageable pageable) {
        return roleGroupConverter.toDto(roleGroupService.search(null, pageable));
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

    @PutMapping
    @ResponseStatus(CREATED)
    public RoleGroupDto update(
            @PathVariable("id") Long id,
            @RequestBody RoleGroupDto data) {
        return roleGroupConverter.toDto(roleGroupService.update(id, data));
    }

    @GetMapping("{id}")
    public RoleGroupDto findById(@PathVariable("id") Long id) {
        return roleGroupConverter.toDto(roleGroupService.findById(id).orElseThrow(DataNotExistException::new));
    }
}
