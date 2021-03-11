package com.dm.uap.controller;

import com.dm.common.dto.ValidationResult;
import com.dm.common.exception.DataNotExistException;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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
     *
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
     *
     * @param id 部门ID
     * @return 部门信息
     */
    @GetMapping("{id}")
    public DepartmentDto findById(@PathVariable("id") Long id) {
        return departmentService.findById(id).orElseThrow(DataNotExistException::new);
    }

    /**
     * 更新部门
     *
     * @param id   要更新的部门的ID
     * @param data 部门信息
     * @return 部门信息
     */
    @PutMapping("{id}")
    @ResponseStatus(CREATED)
    public DepartmentDto update(@PathVariable("id") Long id, @RequestBody DepartmentDto data) {
        return departmentService.update(id, data);
    }

    /**
     * 删除一个部门
     *
     * @param id 要删除的部门的ID
     */
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        departmentService.deleteById(id);
    }

    /**
     * 获取部门列表
     *
     * @param key      搜索关键字
     * @param parentId 上级部门
     * @param pageable 下级部门
     * @return 分页列出的部门信息
     */
    @GetMapping
    public Page<DepartmentDto> search(@RequestParam(value = "keyword", required = false) String key,
                                      @RequestParam(value = "parentId", required = false) Long parentId,
                                      @PageableDefault(sort = "order", direction = Sort.Direction.ASC) Pageable pageable) {
        return departmentService.find(parentId, key, pageable);
    }

    /**
     * 获取所有的部门信息
     *
     * @param pageable 分页参数
     * @return 部门信息的列表
     */
    @GetMapping(params = "scope=all")
    public List<DepartmentDto> listAll(@PageableDefault(size = 10000, sort = "order", direction = Sort.Direction.ASC) Pageable pageable) {
        return departmentService.findAll();
    }

    /**
     * 验证名称是否被占用
     *
     * @param fullname 要验证的名称
     * @param parentId 要验证的父级的范围
     * @param exclude  要排除的id
     * @return 验证结果
     * @apiNote 验证部门名称是否被占用，在同一个父级部门下，不允许有同名部门存在
     */
    @GetMapping(value = "validation", params = "fullname")
    public ValidationResult validationFullName(
            @RequestParam("fullname") String fullname,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "exclude", required = false) Long exclude) {
        if (departmentService.existsByNameAndParent(fullname, parentId, exclude)) {
            return ValidationResult.failure("部门名称已经被占用");
        } else {
            return ValidationResult.success();
        }
    }
}

