package com.dm.uap.controller;

import com.dm.uap.entity.Role;
import com.dm.uap.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RequiredArgsConstructor
@RestController
@RequestMapping({"roles", "/u/roles"})
@Validated
public class RoleController {

    private final RoleService roleService;

    @PutMapping("{id}")
    public Role role(@PathVariable("id") @Min(4) Long id, @RequestBody Role role) {
        roleService.update(id, role);
        role.setId(id);
        return role;
    }
}
