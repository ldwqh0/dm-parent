package com.dm.uap.service.impl;

import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QDepartment;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.service.DepartmentService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final QDepartment qDepartment = QDepartment.department;

    private final DepartmentRepository departmentRepository;

    private final DepartmentConverter departmentConverter;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentConverter departmentConverter) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
    }

    @Override
    @Transactional
    public Department save(DepartmentDto data) {
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
        return dep;
    }

    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    @Transactional
    public Department update(Long id, DepartmentDto data) {
        Department department = departmentRepository.getOne(id);
        departmentConverter.copyProperties(department, data);
        Department parent = Optional.ofNullable(data.getParent())
            .map(DepartmentDto::getId)
            .map(departmentRepository::getOne)
            .orElse(null);
        department.setParent(parent);
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public Page<Department> find(Long parentId, String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (!Objects.isNull(parentId)) {
            query.and(qDepartment.parent.id.eq(parentId));
        }
        if (StringUtils.isNotBlank(key)) {
            query.and(qDepartment.fullname.containsIgnoreCase(key)
                .or(qDepartment.shortname.containsIgnoreCase(key))
                .or(qDepartment.description.containsIgnoreCase(key))
            );
        }
        return departmentRepository.findAll(query, pageable);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
