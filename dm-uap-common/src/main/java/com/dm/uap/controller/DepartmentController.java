package com.dm.uap.controller;

import com.dm.collections.Lists;
import com.dm.common.exception.DataNotExistException;
import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    private final DepartmentConverter departmentConverter;

    @Autowired
    public DepartmentController(DepartmentService departmentService, DepartmentConverter departmentConverter) {
        this.departmentService = departmentService;
        this.departmentConverter = departmentConverter;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public DepartmentDto save(@RequestBody DepartmentDto data) {
        return departmentConverter.toDto(departmentService.save(data));
    }

    @GetMapping("{id}")
    public DepartmentDto get(@PathVariable("id") Long id) {
        return departmentConverter.toDto(departmentService.findById(id).orElseThrow(DataNotExistException::new));
    }

    @PutMapping("{id}")
    @ResponseStatus(CREATED)
    public DepartmentDto update(@PathVariable("id") Long id, @RequestBody DepartmentDto data) {
        return departmentConverter.toDto(departmentService.update(id, data));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        departmentService.deleteById(id);
    }

    @GetMapping(params = {"draw"})
    public Page<DepartmentDto> search(@RequestParam(value = "keywords", required = false) String key,
                                      @RequestParam(value = "parentId", required = false) Long parentId,
                                      @PageableDefault Pageable pageable) {
        return departmentService.find(parentId, key, pageable).map(departmentConverter::toDto);
    }

    @GetMapping(params = "!draw")
    public List<DepartmentDto> tree(@PageableDefault(size = 10000) Pageable pageable) {
        return Lists.transform(departmentService.findAll(), departmentConverter::toDto);
    }
}
