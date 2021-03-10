package com.dm.uap.service;

import com.dm.uap.dto.DepartmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
