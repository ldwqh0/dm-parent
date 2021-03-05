package com.dm.auth.controller;

import com.dm.auth.converter.MenuConverter;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.OrderDto;
import com.dm.auth.entity.Menu;
import com.dm.auth.service.MenuService;
import com.dm.collections.Lists;
import com.dm.common.exception.DataNotExistException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dm.auth.dto.OrderDto.Position.DOWN;
import static com.dm.auth.dto.OrderDto.Position.UP;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Api(tags = {"menu"})
@RequestMapping({"menus", "p/menus"})
@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    private final MenuConverter menuConverter;

    /**
     * 保存一个菜单
     *
     * @param menuDto 要保存的菜单信息
     * @return 保存后的菜单信息
     */
    @ApiOperation("保存菜单")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public MenuDto save(@RequestBody MenuDto menuDto) {
        return menuConverter.toDto(menuService.save(menuDto));
    }

    /**
     * 更新一个菜单信息
     *
     * @param id      要更新的菜单的ID
     * @param menuDto 更新后的菜单信息
     * @return 更新完成的菜单信息
     */
    @ApiOperation("更新菜单")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public MenuDto update(@PathVariable("id") long id, @RequestBody MenuDto menuDto) {
        Menu menu = menuService.update(id, menuDto);
        return menuConverter.toDto(menu);
    }

    /**
     * 根据ID获取菜单信息
     *
     * @param id 菜单ID
     * @return 获取到的菜单信息
     */
    @ApiOperation("获取菜单")
    @GetMapping("{id}")
    public MenuDto findById(@PathVariable("id") Long id) {
        return menuService.get(id).map(menuConverter::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 删除一个菜单
     *
     * @param id 要删除的菜单项目
     * @apiNote 删除菜单时要谨慎，因为会同时删除这个菜单下的所有子菜单
     */
    @ApiOperation("删除菜单")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        menuService.delete(id);
    }

    /**
     * 根据关键字查询子菜单
     *
     * @param pageable 分页信息
     * @param keyword  查询关键字
     * @param parentId 上级菜单的Id
     * @return 查询到的菜单列表
     */
    @ApiOperation("根据关键字查询菜单")
    @GetMapping(params = {"draw"})
    public Page<MenuDto> list(
        @PageableDefault(direction = Direction.ASC, sort = "order") Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "parentId", required = false) Long parentId) {
        return menuService.search(parentId, keyword, pageable).map(menuConverter::toDto);
    }

    /**
     * 更新部分菜单信息
     *
     * @param id    要更新的菜单的id
     * @param _menu 要更新菜单信息
     * @return 更新后的菜单信息
     */
    @ApiOperation("更新菜单部分信息")
    @PatchMapping("{id}")
    public MenuDto patch(@PathVariable("id") long id, @RequestBody MenuDto _menu) {
        return menuConverter.toDto(menuService.patch(id, _menu));
    }

    /**
     * 获取所有可用的菜单树
     *
     * @param sort 排序方式
     * @return 所有的菜单的列表
     */
    @ApiOperation("获取可用菜单树")
    @GetMapping
    public List<MenuDto> getAllMenuEnabled(@SortDefault(direction = Direction.ASC, sort = {"order"}) Sort sort) {
        List<Menu> allMenuEnabled = menuService.listAllEnabled(sort);
        return Lists.transform(allMenuEnabled, menuConverter::toDto);
    }

    /**
     * 移动菜单
     *
     * @param id    要移动的菜单
     * @param order 移动的方向
     * @return 移动后的菜单值
     */
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
