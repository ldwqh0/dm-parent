package com.dm.auth.controller;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.PositionRequest;
import com.dm.auth.service.MenuService;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.common.validation.ValidationResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dm.auth.dto.PositionRequest.Position.DOWN;
import static com.dm.auth.dto.PositionRequest.Position.UP;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * 菜单管理
 */
@RequestMapping({"menus", "p/menus"})
@RestController
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 保存一个菜单
     *
     * @param menuDto 要保存的菜单信息
     * @return 保存后的菜单信息
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public MenuDto save(@RequestBody MenuDto menuDto) {
        return menuService.save(menuDto);
    }

    /**
     * 更新一个菜单信息
     *
     * @param id      要更新的菜单的ID
     * @param menuDto 更新后的菜单信息
     * @return 更新完成的菜单信息
     */
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public MenuDto update(@PathVariable("id") long id, @RequestBody MenuDto menuDto) {
        return menuService.update(id, menuDto);
    }

    /**
     * 根据ID获取菜单信息
     *
     * @param id 菜单ID
     * @return 获取到的菜单信息
     */
    @GetMapping("{id}")
    public MenuDto findById(@PathVariable("id") Long id) {
        return menuService.findById(id).orElseThrow(DataNotExistException::new);
    }

    /**
     * 删除一个菜单
     *
     * @param id 要删除的菜单项目
     * @apiNote 删除菜单时要谨慎，因为会同时删除这个菜单下的所有子菜单
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        menuService.delete(id);
    }

    /**
     * 更新部分菜单信息
     *
     * @param id   要更新的菜单的id
     * @param menu 要更新菜单信息
     * @return 更新后的菜单信息
     * @apiNote 当前只能改变菜单的enabled状态
     */
    @PatchMapping("{id}")
    public MenuDto patch(@PathVariable("id") long id, @RequestBody MenuDto menu) {
        return menuService.patch(id, menu);
    }

    /**
     * 根据关键字查询子菜单
     *
     * @param pageable 分页信息
     * @param keyword  查询关键字
     * @param parentId 上级菜单的Id
     * @return 查询到的菜单列表
     */
    @GetMapping
    public Page<MenuDto> list(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                              @RequestParam(value = "enabled", required = false) Boolean enabled,
                              @RequestParam(value = "parentId", required = false) Long parentId,
                              @PageableDefault(direction = Direction.ASC, sort = "order") Pageable pageable) {
        return menuService.search(parentId, enabled, keyword, pageable);
    }

    /**
     * 获取某个菜单的子菜单树
     *
     * @param sort     排序方式,默认按照order排序，暂不支持改变，也不要传入
     * @param parentId 要获取的节点的id,如果不传入，则获取所有可以用菜单
     * @param enabled  是否仅仅获取可以用的菜单项，为否或者为空表示获取所有菜单项
     * @return 所有的菜单的列表
     */
    @GetMapping(params = {"scope=all"})
    public List<MenuDto> list(@RequestParam(value = "parentId", required = false) Long parentId,
                              @RequestParam(value = "enabled", required = false) Boolean enabled,
                              @SortDefault(direction = Direction.ASC, sort = {"order"}) Sort sort) {
        return menuService.listOffspring(parentId, enabled, sort);
    }

    /**
     * 移动菜单
     *
     * @param id    要移动的菜单
     * @param order 移动的方向
     * @return 移动后的菜单值
     */
    @PutMapping("{id}/order")
    public MenuDto order(@PathVariable("id") Long id, @RequestBody PositionRequest order) {
        if (UP.equals(order.getPosition())) {
            return menuService.move(id, UP);
        } else if (DOWN.equals(order.getPosition())) {
            return menuService.move(id, DOWN);
        } else {
            throw new DataValidateException("error");
        }
    }

    /**
     * 对菜单名字进行校验
     *
     * @param name    要校验的菜单的名称
     * @param exclude 要排除的菜单id
     * @return 验证结果
     */
    @GetMapping(value = "validation", params = {"name"})
    public ValidationResult validate(@RequestParam("name") String name,
                                     @RequestParam(value = "exclude", required = false) Long exclude) {
        if (menuService.existsByName(name, exclude)) {
            return ValidationResult.failure("指定名称已经被占用");
        } else {
            return ValidationResult.success();
        }
    }
}
