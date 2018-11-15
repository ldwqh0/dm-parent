package com.dm.springboot.autoconfigure.uap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.dm.uap.dto.MenuAuthorityDto;
import com.dm.uap.dto.MenuDto;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.Menu;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.entity.Role.Status;
import com.dm.uap.service.AuthorityService;
import com.dm.uap.service.MenuService;
import com.dm.uap.service.RoleService;
import com.dm.uap.service.UserService;

@Configuration
@ConditionalOnClass(User.class)
@EntityScan({ "com.dm.uap" })
@EnableJpaRepositories({ "com.dm.uap" })
@ComponentScan({ "com.dm.uap" })
public class UapAutoConfiguration {
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private AuthorityService authorityService;

	/**
	 * 初始化默认用户
	 */
	@PostConstruct
	public void initData() {
		// 初始化角色
		initRole();
		// 初始化用户
		initUser();

		// 初始化菜单
		initMenu();

		// 初始化用户菜单授权信息
		initMenuAuthority();
	}

	private void initMenuAuthority() {
		Optional<Role> role = roleService.getFirst();
		List<Menu> menus = menuService.listAllEnabled(Sort.by("order"));
		if (!authorityService.exists() && role.isPresent()) {
			MenuAuthorityDto authority = new MenuAuthorityDto();
			authority.setRoleId(role.get().getId());
			List<MenuDto> menus_ = menus.stream().map(m -> {
				MenuDto md = new MenuDto();
				md.setId(m.getId());
				return md;
			}).collect(Collectors.toList());
			authority.setAuthorityMenus(menus_);
			authorityService.save(authority);
		}
	}

	private void initMenu() {
		if (!menuService.exists()) {
			MenuDto menu = new MenuDto();
			menu.setName("home");
			menu.setEnabled(Boolean.TRUE);
			menu.setTitle("首页");
			menu.setUrl("/");
			menuService.save(menu);
		}
	}

	private void initRole() {
		if (!roleService.exist()) {
			RoleDto dto = new RoleDto();
			dto.setName("ROLE_ADMIN");
			dto.setState(Status.ENABLED);
			roleService.save(dto);
		}
	}

	private void initUser() {
		if (!userService.exist()) {
			UserDto user = new UserDto();
			user.setUsername("admin");
			user.setFullname("系统管理员");
			user.setPassword("123456");
			user.setEnabled(true);

			if (roleService.exist()) {
				Role _role = roleService.getFirst().get();
				RoleDto role = new RoleDto();
				role.setId(_role.getId());
				user.setRoles(Collections.singletonList(role));
			}

			userService.save(user);
		}
	}

}
