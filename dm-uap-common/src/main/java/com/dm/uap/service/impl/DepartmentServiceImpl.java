package com.dm.uap.service.impl;

import com.dm.collections.CollectionUtils;
import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QDepartment;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.service.DepartmentService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 */
// todo 这个数据不会经常变动，可能需要使用缓存
// 使用dto返回数据，方便缓存
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final QDepartment qDepartment = QDepartment.department;

    private final DepartmentRepository departmentRepository;

    private final DepartmentConverter departmentConverter;

    @Override
    @Transactional
    public DepartmentDto save(DepartmentDto data) {
        Department department = new Department();
        departmentConverter.copyProperties(department, data);
        DepartmentDto parent = data.getParent();
        if (Objects.isNull(parent)) {
            department.setParent(null);
        } else {
            department.setParent(departmentRepository.getOne(parent.getId()));
        }
        Department dep = departmentRepository.save(department);
        // 设置部门的顺序
        dep.setOrder(dep.getId());
        return this.toDto(dep);
    }

    @Override
    public Optional<DepartmentDto> findById(Long id) {
        return departmentRepository.findById(id).map(departmentConverter::toDto);
    }

    @Override
    @Transactional
    public DepartmentDto update(Long id, DepartmentDto data) {
        Department department = departmentRepository.getOne(id);
        departmentConverter.copyProperties(department, data);
        Department parent = Optional.ofNullable(data.getParent())
            .map(DepartmentDto::getId)
            .map(departmentRepository::getOne)
            .orElse(null);
        department.setParent(parent);
        return this.toDto(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public Page<DepartmentDto> find(Long parentId, String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (Objects.isNull(parentId)) {
            query.and(qDepartment.parent.isNull());
        } else {
            query.and(qDepartment.parent.id.eq(parentId));
        }
        if (StringUtils.isNotBlank(key)) {
            query.and(qDepartment.fullname.containsIgnoreCase(key)
                .or(qDepartment.shortname.containsIgnoreCase(key))
                .or(qDepartment.description.containsIgnoreCase(key))
            );
        }
        return departmentRepository.findAll(query, pageable).map(this::toDto);
    }

    @Override
    public List<DepartmentDto> findAll() {
        return CollectionUtils.transform(departmentRepository.findAll(), this::toDto);
    }

    private DepartmentDto toDto(Department department) {
        DepartmentDto result = departmentConverter.toDto(department);
        assert result != null;
        long childrenCount = departmentRepository.childrenCounts(department);
        result.setHasChildren(childrenCount > 0);
        result.setChildrenCount(childrenCount);
        return result;
    }
}
