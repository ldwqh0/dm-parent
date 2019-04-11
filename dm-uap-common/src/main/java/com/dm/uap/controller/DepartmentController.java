package com.dm.uap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.service.DepartmentService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("departments")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DepartmentConverter departmentConverter;

	@PostMapping
	@ResponseStatus(CREATED)
	public DepartmentDto save(@RequestBody DepartmentDto data) {
		return departmentConverter.toDto(departmentService.save(data));
	}

	@GetMapping("{id}")
	public DepartmentDto get(@PathVariable("id") Long id) {
		return departmentConverter.toDto(departmentService.findById(id));
	}

	@PutMapping
	@ResponseStatus(CREATED)
	public DepartmentDto update(@PathVariable("id") Long id, DepartmentDto data) {
		return departmentConverter.toDto(departmentService.update(id, data));
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id) {
		departmentService.deleteById(id);
	}

}
