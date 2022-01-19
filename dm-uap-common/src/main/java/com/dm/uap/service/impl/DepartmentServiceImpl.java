package com.dm.uap.service.impl;

import com.dm.collections.CollectionUtils;
import com.dm.collections.Lists;
import com.dm.common.exception.DataValidateException;
import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QDepartment;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.service.DepartmentService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final QDepartment qDepartment = QDepartment.department;

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DepartmentDto save(DepartmentDto data) {
        Department department = copyProperties(new Department(), data);
        data.getParent().map(departmentRepository::getByDto).ifPresent(department::setParent);
        department.setDirector(data.getDirector());
        Department dep = departmentRepository.save(department);
        // 设置部门的顺序
        dep.setOrder(dep.getId());
        return this.toDto(dep);
    }


    @Override
    public Optional<DepartmentDto> findById(Long id) {
        return departmentRepository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DepartmentDto update(Long id, DepartmentDto data) {
        preCheck(data);
        Department department = copyProperties(departmentRepository.getById(id), data);
        // 更新时先清空父级和负责人的原始
        department.setParent(null);
        department.setDirector(null);
        // 重新设置新的配置和原始数据
        data.getParent().map(departmentRepository::getByDto).ifPresent(department::setParent);
        department.setDirector(data.getDirector());
        return this.toDto(departmentRepository.save(department));
    }

    // 保存前对进行前置校验，避免节点递归
    private void preCheck(DepartmentDto data) {
        Long departmentId = data.getId();
        if (Objects.nonNull(departmentId)) {
            Optional<Department> parent = data.getParent()
                .map(DepartmentDto::getId)
                .flatMap(departmentRepository::findById);
            while (parent.isPresent()) {
                if (Objects.equals(departmentId, parent.get().getId())) {
                    throw new DataValidateException("不能将一个节点的父级节点设置为它自身或它的叶子节点");
                }
                parent = parent.get().getParent();
            }
        }
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
            query.and(qDepartment.fullName.containsIgnoreCase(key)
                .or(qDepartment.shortName.containsIgnoreCase(key))
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
    public boolean existsByNameAndParent(String fullName, Long parentId, Long exclude) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qDepartment.fullName.eq(fullName));
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

    @Override
    public List<DepartmentDto> listOffspring(Long parentId, Sort sort) {
        if (Objects.isNull(parentId)) {
            return Lists.transform(departmentRepository.findAll(sort), this::toDto);
        } else {
            // todo 这里待处理，需要根据请
            return Collections.emptyList();
        }
    }

    private DepartmentDto toDto(Department department) {
        return DepartmentConverter.toDto(department, departmentRepository.childrenCounts(department), departmentRepository.userCounts(department));
    }

    private Department copyProperties(Department model, DepartmentDto dto) {
        model.setFullName(dto.getFullName());
        model.setShortName(dto.getShortname());
        model.setDescription(dto.getDescription());
        model.setType(dto.getType());
        model.setLogo(dto.getLogo());
        return model;
    }

}
