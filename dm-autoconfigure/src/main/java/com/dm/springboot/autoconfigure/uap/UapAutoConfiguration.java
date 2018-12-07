package com.dm.springboot.autoconfigure.uap;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.dm.uap.dto.RoleDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.entity.Role.Status;
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

	/**
	 * 初始化默认用户
	 */
	@PostConstruct
	public void initData() {
		// 初始化角色
		initRole();
		// 初始化用户
		initUser();
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
