package com.dm.uap.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.converter.AuthorityConverter;
import com.dm.uap.converter.MenuConverter;
import com.dm.uap.dto.MenuAuthorityDto;
import com.dm.uap.dto.MenusTreeDto;
import com.dm.uap.entity.Menu;
import com.dm.uap.entity.Authority;
import com.dm.uap.service.AuthorityService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping({ "menuAuthorities", "oauth2/menuAuthorities" })
public class MenuAuthorityController {

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private MenuConverter menuConverter;

	@Autowired
	private AuthorityConverter authorityConverter;

	/**
	 * 保存一个角色的菜单权限配置
	 * 
	 * @param authorityDto
	 * @return
	 */
	@PutMapping("{roleId}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ResponseStatus(code = HttpStatus.CREATED)
	public MenuAuthorityDto save(@PathVariable("roleId") Long roleId, @RequestBody MenuAuthorityDto authorityDto) {
		Authority menuAuthority = authorityService.save(authorityDto);
		return authorityConverter.toMenuAuthorityDto(menuAuthority);
	}

	/**
	 * 获取某个角色的可视菜单，<br />
	 * 
	 * 只会列出明确标记为选中的菜单项。
	 * 
	 * @param id 角色ID
	 * @return
	 */
	@GetMapping("{roleId}")
	public MenuAuthorityDto get(@PathVariable("roleId") Long roleId) {
		Optional<Authority> authority = authorityService.get(roleId);
		return authorityConverter.toMenuAuthorityDto(authority);
	}

	/**
	 * 获取当前用户的可用菜单项，当某个子菜单可用时，父菜单也会被列出来
	 * 
	 * Description: 根据登陆用户获取菜单
	 * 
	 * @return
	 */
	@ApiOperation("获取当前用户的可用菜单项")
	@GetMapping("menus")
	public List<MenusTreeDto> systemMenu(@CurrentUser UserDetailsDto userDto) {
		List<Menu> menus = authorityService.listUserMenusTree(userDto.getId());
		return menuConverter.toAuthorityMenusDto(menus);
	}
}