package com.dm.uap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.common.dto.TableResult;
import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.dto.DepartmentTreeDto;
import com.dm.uap.service.DepartmentService;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

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

	@GetMapping(params = { "draw" })
	public TableResult<DepartmentDto> search(
			@RequestParam("draw") Long draw,
			@RequestParam(value = "keywords", required = false) String key,
			@PageableDefault Pageable pageable) {
		return TableResult.success(draw, departmentService.find(key, pageable), departmentConverter::toDto);
	}

	@GetMapping(params = "type=tree")
	public List<DepartmentTreeDto> tree(
			@PageableDefault(size = 10000) Pageable pageable) {
		return departmentConverter.toTree(departmentService.findAll());
	}
}
