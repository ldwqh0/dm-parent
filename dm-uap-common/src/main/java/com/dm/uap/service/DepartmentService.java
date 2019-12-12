package com.dm.uap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;

public interface DepartmentService {

    public Department save(DepartmentDto data);

    public Optional<Department> findById(Long id);

    public Department update(Long id, DepartmentDto data);

    public void deleteById(Long id);

    public Page<Department> find(String key, Pageable pageable);

    public List<Department> findAll();

}
