package com.dm.uap.controller;

import com.dm.common.exception.DataNotExistException;
import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * 部门管理
 */
@RestController
@RequestMapping("departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 新增部门
     * @param data 新部门的信息
     * @return
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public DepartmentDto save(@RequestBody DepartmentDto data) {
        return departmentService.save(data);
    }

    /**
     * 获取部门
     * @param id 部门ID
     *
     * @return 部门信息
     */
    @GetMapping("{id}")
    public DepartmentDto get(@PathVariable("id") Long id) {
        return departmentService.findById(id).orElseThrow(DataNotExistException::new);
    }

    @PutMapping("{id}")
    @ResponseStatus(CREATED)
    public DepartmentDto update(@PathVariable("id") Long id, @RequestBody DepartmentDto data) {
        return departmentService.update(id, data);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        departmentService.deleteById(id);
    }

    @GetMapping
    public Page<DepartmentDto> search(@RequestParam(value = "keywords", required = false) String key,
                                      @RequestParam(value = "parentId", required = false) Long parentId,
                                      @PageableDefault(sort = "order", direction = Sort.Direction.ASC) Pageable pageable) {
        return departmentService.find(parentId, key, pageable);
    }

    @GetMapping(params = "scope=all")
    public List<DepartmentDto> listAll(@PageableDefault(size = 10000, sort = "order", direction = Sort.Direction.ASC) Pageable pageable) {
        return departmentService.findAll();
    }
}
