package com.dm.uap.service;

import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Department save(DepartmentDto data);

    Optional<Department> findById(Long id);

    Department update(Long id, DepartmentDto data);

    void deleteById(Long id);

    Page<Department> find(String key, Pageable pageable);

    List<Department> findAll();

}
