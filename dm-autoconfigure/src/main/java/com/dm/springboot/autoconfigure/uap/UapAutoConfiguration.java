package com.dm.springboot.autoconfigure.uap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.dm.uap.dto.RegionDto;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.entity.Role.Status;
import com.dm.uap.service.RegionService;
import com.dm.uap.service.RoleService;
import com.dm.uap.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(User.class)
@EntityScan({ "com.dm.uap" })
@EnableJpaRepositories({ "com.dm.uap" })
@ComponentScan({ "com.dm.uap" })
@EnableConfigurationProperties({ DefaultUserProperties.class })
@Slf4j
public class UapAutoConfiguration {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DefaultUserProperties defaultUser;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private RegionService regionService;

	/**
	 * 初始化默认用户
	 */
	@PostConstruct
	public void initData() {
		initRegion();
		// 初始化角色
		initRole();
		// 初始化用户
		initUser();
	}

	private void initRole() {
		// 增加默认管理员角色
		if (!roleService.findByName("ROLE_ADMIN").isPresent()) {
			RoleDto role = new RoleDto();
			role.setName("ROLE_ADMIN");
			role.setState(Status.ENABLED);
			roleService.save(role);
		}
		// 增加默认普通用户角色
		if (!roleService.findByName("ROLE_USER").isPresent()) {
			RoleDto role = new RoleDto();
			role.setName("ROLE_USER");
			role.setState(Status.ENABLED);
			roleService.save(role);
		}
		// 增加默认匿名用户角色
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

	// 初始化用户的区划
	private void initRegion() {
		if (!regionService.existAny()) {
			try (InputStream iStream = this.getClass().getClassLoader().getResourceAsStream("regions.json")) {
				MapType elementType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class,
						String.class);
				CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,
						elementType);
				List<Map<String, Object>> result = objectMapper.readValue(iStream, collectionType);
				if (CollectionUtils.isNotEmpty(result)) {
					List<RegionDto> regions = result.stream().map(r -> {
						RegionDto region = new RegionDto();
						region.setName(String.valueOf(r.get("name")));
						region.setCode(String.valueOf(r.get("code")));
						region.setLatitude(Double.valueOf(String.valueOf(r.get("lat"))));
						region.setLongitude(Double.valueOf(String.valueOf(r.get("lng"))));
						String parentCode = String.valueOf(r.get("parent"));
						if (StringUtils.isNotBlank(parentCode)) {
							RegionDto parent = new RegionDto();
							parent.setCode(parentCode);
							region.setParent(parent);
						}
						return region;
					}).collect(Collectors.toList());
					regionService.save(regions);
				}
			} catch (IOException e) {
				log.error("解析json文件时发生错误", e);
			}
		}
	}
}
