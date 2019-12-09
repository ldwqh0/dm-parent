package com.dm.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.converter.MenuConverter;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.OrderDto;
import com.dm.auth.entity.Menu;
import com.dm.auth.service.MenuService;
import com.dm.common.exception.DataNotExistException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static org.springframework.http.HttpStatus.*;
import static com.dm.auth.dto.OrderDto.Position.*;

import java.util.List;

@Api(tags = { "menu" })
@RequestMapping({ "menus", "p/menus" })
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuConverter menuConverter;

    @ApiOperation("保存菜单")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public MenuDto save(@RequestBody MenuDto menuDto) {
        return menuConverter.toDto(menuService.save(menuDto));
    }

    @ApiOperation("更新菜单")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public MenuDto update(@PathVariable("id") long id, @RequestBody MenuDto menuDto) {
        Menu menu = menuService.update(id, menuDto);
        return menuConverter.toDto(menu);
    }

    @ApiOperation("获取菜单")
    @GetMapping("{id}")
    public MenuDto get(@PathVariable("id") Long id) {
        return menuConverter.toDto(menuService.get(id).orElseThrow(DataNotExistException::new));
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        menuService.delete(id);
    }

    @ApiOperation("根据关键字查询菜单")
    @GetMapping(params = { "draw" })
    public Page<MenuDto> list(
            @PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "order") Pageable pageable,
            @RequestParam(value = "search", required = false) String key,
            @RequestParam(value = "parentId", required = false) Long parentId) {
        Page<Menu> result = menuService.search(parentId, key, pageable);
        return result.map(menuConverter::toDto);
    }

    @ApiOperation("更新菜单部分信息")
    @PatchMapping("{id}")
    public MenuDto patch(@PathVariable("id") long id, @RequestBody MenuDto _menu) {
        Menu menu = menuService.patch(id, _menu);
        return menuConverter.toDto(menu);
    }

    @ApiOperation("获取可用菜单树")
    @GetMapping
    public List<MenuDto> getAllMenuEnabled(@SortDefault(direction = Direction.ASC, sort = { "order" }) Sort sort) {
        List<Menu> allMenuEnabled = menuService.listAllEnabled(sort);
        return menuConverter.toDto(allMenuEnabled);
    }

    @PutMapping("{id}/order")
    @ApiOperation("移动菜单")
    public MenuDto order(@PathVariable("id") Long id, @RequestBody OrderDto order) {
        Menu menu = null;
        if (UP.equals(order.getPosition())) {
            menu = menuService.moveUp(id);
        } else if (DOWN.equals(order.getPosition())) {
            menu = menuService.moveDown(id);
        }
        return menuConverter.toDto(menu);
    }

}
