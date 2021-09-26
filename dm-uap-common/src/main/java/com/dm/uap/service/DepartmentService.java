package com.dm.uap.service;

import com.dm.uap.dto.DepartmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    DepartmentDto save(DepartmentDto data);

    Optional<DepartmentDto> findById(Long id);

    DepartmentDto update(Long id, DepartmentDto data);

    void deleteById(Long id);

    Page<DepartmentDto> find(Long parentId, String key, Pageable pageable);

    List<DepartmentDto> findAll();

    boolean existsByNameAndParent(String fullname, Long parentId, Long exclude);

    /**
     * 获取某个部门的下级部门树
     *
     * @param sort     排序方式,默认按照order排序，暂不支持改变，也不要传入
     * @param parentId 要获取的节点的id,如果不传入，则获取所有部门
     * @return 所有的菜单的列表
     */
    List<DepartmentDto> listOffspring(Long parentId, Sort sort);
}
