package com.dm.uap.service.impl;

import com.dm.collections.CollectionUtils;
import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QDepartment;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.repository.UserRepository;
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
// 使用dto返回数据，方便缓存
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final QDepartment qDepartment = QDepartment.department;

    private final DepartmentRepository departmentRepository;

    private final DepartmentConverter departmentConverter;

    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DepartmentDto save(DepartmentDto data) {
        Department department = new Department();
        departmentConverter.copyProperties(department, data);
        data.getParent().map(departmentRepository::getByDto).ifPresent(department::setParent);
        data.getDirector().map(userRepository::getByDto).ifPresent(department::setDirector);
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
    @Transactional(rollbackFor = Throwable.class)
    public DepartmentDto update(Long id, DepartmentDto data) {
        Department department = departmentRepository.getOne(id);
        departmentConverter.copyProperties(department, data);
        // 更新时先清空父级和负责人的原始
        department.setParent(null);
        department.setDirector(null);
        // 重新设置新的配置和原始数据
        data.getParent().map(departmentRepository::getByDto).ifPresent(department::setParent);
        data.getDirector().map(userRepository::getByDto).ifPresent(department::setDirector);
        return this.toDto(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    public boolean existsByNameAndParent(String fullname, Long parentId, Long exclude) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qDepartment.fullname.eq(fullname));
        if (Objects.isNull(parentId)) {
            query.and(qDepartment.parent.isNull());
        } else {
            query.and(qDepartment.parent.id.eq(parentId));
        }
        if (!Objects.isNull(exclude)) {
            query.and(qDepartment.id.ne(exclude));
        }
        return departmentRepository.exists(query);
    }

    private DepartmentDto toDto(Department department) {
        DepartmentDto result = departmentConverter.toDto(department);
        assert result != null;
        long childrenCount = departmentRepository.childrenCounts(department);
        result.setHasChildren(childrenCount > 0);
        result.setChildrenCount(childrenCount);
        long userCount = departmentRepository.userCounts(department);
        result.setUserCount(userCount);
        return result;
    }
}
