package com.dm.springboot.autoconfigure.uap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.dm.security.access.RequestAuthorityAttribute.MatchType;
import com.dm.uap.dto.MenuAuthorityDto;
import com.dm.uap.dto.MenuDto;
import com.dm.uap.dto.ResourceAuthorityDto;
import com.dm.uap.dto.ResourceDto;
import com.dm.uap.dto.ResourceOperationDto;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.Menu;
import com.dm.uap.entity.Resource;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.entity.Role.Status;
import com.dm.uap.service.AuthorityService;
import com.dm.uap.service.MenuService;
import com.dm.uap.service.ResourceService;
import com.dm.uap.service.RoleService;
import com.dm.uap.service.UserService;

@Configuration
@ConditionalOnClass(User.class)
@EntityScan({ "com.dm.uap" })
@EnableJpaRepositories({ "com.dm.uap" })
@ComponentScan({ "com.dm.uap" })
@EnableConfigurationProperties({ DefaultUserProperties.class })
public class UapAutoConfiguration {

	@Autowired
	private DefaultUserProperties defaultUser;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private ResourceService resourceService;

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

		initResource();

		// 初始化用户菜单授权信息
		initAuthority();

	}

	private void initResource() {
		if (!resourceService.exist()) {
			ResourceDto resource = new ResourceDto();
			resource.setName("default");
			resource.setMatchType(MatchType.ANT_PATH);
			resource.setMatcher("/**");
			resource.setDescription("默认资源类型");
			resourceService.save(resource);
		}
	}

	private void initAuthority() {
		Optional<Role> role = roleService.findByName("ROLE_ADMIN");
		List<Menu> menus = menuService.listAllEnabled(Sort.by("order"));
		// 判断是否
		if (!authorityService.exists() && role.isPresent()) {
			Long roleId = role.get().getId();
			MenuAuthorityDto menuAuthority = new MenuAuthorityDto();
			menuAuthority.setRoleId(roleId);
			List<MenuDto> menus_ = menus.stream().map(m -> {
				MenuDto md = new MenuDto();
				md.setId(m.getId());
				return md;
			}).collect(Collectors.toList());

			// 初始化管理员角色的资源权限，默认授予default资源的全部权限
			Optional<Resource> resource = resourceService.findByName("default");
			if (resource.isPresent()) {

				ResourceDto resourceDto = new ResourceDto();
				resourceDto.setId(resource.get().getId());
				// 组合一组资源授权
				ResourceOperationDto resourceOperation = new ResourceOperationDto();
				resourceOperation.setReadable(Boolean.TRUE);
				resourceOperation.setSaveable(Boolean.TRUE);
				resourceOperation.setUpdateable(Boolean.TRUE);
				resourceOperation.setDeleteable(Boolean.TRUE);
				resourceOperation.setResource(resourceDto);

				// 将知道的授权组装为一个授权对象
				ResourceAuthorityDto resourceAuthority = new ResourceAuthorityDto();
				resourceAuthority.setRoleId(roleId);
				resourceAuthority.setResourceAuthorities(Collections.singletonList(resourceOperation));
				authorityService.save(resourceAuthority);
			}

			menuAuthority.setAuthorityMenus(menus_);
			authorityService.save(menuAuthority);
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
		if (!roleService.findByName("ROLE_ADMIN").isPresent()) {
			RoleDto role = new RoleDto();
			role.setName("ROLE_ADMIN");
			role.setState(Status.ENABLED);
			roleService.save(role);
		}
		if (!roleService.findByName("ROLE_ANONYMOUS").isPresent()) {
			RoleDto role = new RoleDto();
			role.setName("ROLE_ANONYMOUS");
			role.setState(Status.ENABLED);
			roleService.save(role);
		}
	}

	private void initUser() {
		String username = defaultUser.getUsername();
		String password = defaultUser.getPassword();
		String fullname = defaultUser.getFullname();

		if (!userService.exist()) {
			UserDto user = new UserDto();
			user.setUsername(username);
			user.setFullname(fullname);
			user.setPassword(password);
			user.setEnabled(true);
			Optional<Role> _role = roleService.findByName("ROLE_ADMIN");
			if (_role.isPresent()) {
				RoleDto role = new RoleDto();
				role.setId(_role.get().getId());
				user.setRoles(Collections.singletonList(role));
			}
			userService.save(user);
		}
	}

}
