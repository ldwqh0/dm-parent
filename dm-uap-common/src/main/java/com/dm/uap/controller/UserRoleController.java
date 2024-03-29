package com.dm.uap.controller;

import com.dm.uap.dto.UserRoleDto;
import com.dm.uap.service.UserRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping({"/u/roles"})
@Validated
public class UserRoleController {

    private final UserRoleService roleService;

    public UserRoleController(UserRoleService roleService) {
        this.roleService = roleService;
    }

    @PutMapping("{id}")
    public UserRoleDto role(@PathVariable("id") @Min(4) Long id, @RequestBody UserRoleDto role) {
        return roleService.update(id, role);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") @Min(4) Long id) {
        roleService.delete(id);
    }
}
