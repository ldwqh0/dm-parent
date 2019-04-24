package com.dm.uap.dingtalk.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.dingtalk.api.request.OapiUserCreateRequest;
import com.dm.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dm.dingtalk.api.response.OapiUserGetResponse;
import com.dm.dingtalk.api.response.OapiUserGetResponse.Roles;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.uap.dingtalk.converter.DUserConverter;
import com.dm.uap.dingtalk.entity.DDepartment;
import com.dm.uap.dingtalk.entity.DRole;
import com.dm.uap.dingtalk.entity.DUser;
import com.dm.uap.dingtalk.repository.DDepartmentRepository;
import com.dm.uap.dingtalk.repository.DRoleRepository;
import com.dm.uap.dingtalk.repository.DUserRepository;
import com.dm.uap.dingtalk.service.DUserService;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.repository.UserRepository;

@Service
public class DUserServiceImpl implements DUserService {

	@Autowired
	private DingTalkService dingTalkService;

	@Autowired
	private DDepartmentRepository dDepartmentRepository;

	@Autowired
	private DUserRepository dUserRepository;

	@Autowired
	private DUserConverter dUserConverter;

	@Autowired
	private DRoleRepository dRoleRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@Override
	public void sync() {
		syncToUap(fetch());
	}

	@Override
	public DUser createUser(DUser dUser) {
		User user = toUser(dUser);
		dUser.setUser(user);
		// 保存钉钉用户信息到本地
		dUser = dUserRepository.save(dUser);
		// 将用户信息保存到钉钉
		saveToDingTalk(dUser);
		return dUser;
	}

	private void saveToDingTalk(DUser dUser) {
		OapiUserCreateRequest request = dUserConverter.toOapiUserCreateRequest(dUser);
		dingTalkService.createUser(request);
	}

	// 将钉钉用户信息同步到系统uap
	private List<User> syncToUap(List<DUser> dUsers) {
		List<User> users = dUsers.stream().map(this::toUser).collect(Collectors.toList());
		return userRepository.saveAll(users);
	}

	private User toUser(DUser dUser) {
		User user = dUser.getUser();
		if (Objects.isNull(user)) {
			user = new User();
			dUser.setUser(user);
		}
		dUserConverter.copyProperties(user, dUser);
		// 设置部门顺序
		Map<DDepartment, Long> _orders = dUser.getOrderInDepts();
		Map<Department, Long> orders = new HashMap<>();
		_orders.entrySet().forEach(e -> {
			orders.put(e.getKey().getDepartment(), e.getValue());
		});
		user.setOrders(orders);

		// 设置职务信息
		Map<Department, String> post = new HashMap<Department, String>();
		String pos = dUser.getPosition();
		dUser.getDepartments().forEach(d -> {
			post.put(d.getDepartment(), pos);
		});
		user.setPosts(post);
		// 设置角色
		Set<DRole> dRoles = dUser.getRoles();
		if (CollectionUtils.isNotEmpty(dRoles)) {
			List<Role> roles = dRoles.stream().map(DRole::getRole).collect(Collectors.toList());
			user.setRoles(roles);
		}
		return user;
	}

	// 从服务器拉取钉钉用户信息
	private List<DUser> fetch() {
		Set<String> userIds = getUserIds();
		dUserRepository.deleteByIdNotIn(userIds);
		List<DUser> users = userIds.stream()
				.map(userId -> dUserRepository.existsById(userId) ? dUserRepository.getOne(userId) : new DUser(userId))
				.map(dUser -> {
					copyProperties(dUser, dingTalkService.fetchUserById(dUser.getUserid()));
					return dUser;
				}).collect(Collectors.toList());
		return dUserRepository.saveAll(users);
	}

	private Set<String> getUserIds() {
		List<DDepartment> dDepartments = dDepartmentRepository.findAll();
		return dDepartments.stream()
				.map(DDepartment::getId)
				.map(dingTalkService::fetchUsers)
				.map(OapiUserGetDeptMemberResponse::getUserIds)
				.flatMap(List::stream)
				.collect(Collectors.toSet());
	}

	private void copyProperties(DUser dUser, OapiUserGetResponse rsp) {
		dUserConverter.copyProperties(dUser, rsp);
		Set<DDepartment> departments = rsp.getDepartment().stream().map(dDepartmentRepository::getOne)
				.collect(Collectors.toSet());
		dUser.setDepartments(departments);
		// 合并领导信息
		try {
			Map<Long, Boolean> isLeaderMap = parseLeaderMap(rsp.getIsLeaderInDepts());
			if (MapUtils.isNotEmpty(isLeaderMap)) {
				Map<DDepartment, Boolean> dLeaderMap = new HashMap<>();
				for (Entry<Long, Boolean> entry : isLeaderMap.entrySet()) {
					dLeaderMap.put(dDepartmentRepository.getOne(entry.getKey()), entry.getValue());
				}
				dUser.setLeaderInDepts(dLeaderMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 合并排序信息
		try {
			Map<Long, Long> orderMap = parseOrderMap(rsp.getOrderInDepts());
			if (MapUtils.isNotEmpty(orderMap)) {
				Map<DDepartment, Long> dOrderMap = new HashMap<DDepartment, Long>();
				for (Entry<Long, Long> orderEntry : orderMap.entrySet()) {
					dOrderMap.put(dDepartmentRepository.getOne(orderEntry.getKey()), orderEntry.getValue());
				}
				dUser.setOrderInDepts(dOrderMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Roles> roles = rsp.getRoles();
		if (CollectionUtils.isNotEmpty(roles)) {
			Set<DRole> dRoles = roles.stream().map(Roles::getId)
					.map(dRoleRepository::getOne)
					.collect(Collectors.toSet());
			dUser.setRoles(dRoles);
		}
	}

	private Map<Long, Boolean> parseLeaderMap(String v) {
		try {
			Map<Long, Boolean> result = new LinkedHashMap<>();
			if (StringUtils.isNotBlank(v)) {
				String[] kvs = v.split("[,\\{\\}]");
				for (String kv : kvs) {
					if (StringUtils.isNotBlank(kv)) {
						String[] kvArray = kv.split(":");
						result.put(Long.valueOf(kvArray[0]), Boolean.valueOf(kvArray[1]));
					}
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException("can not parse leader info from giving string", e);
		}
	}

	private Map<Long, Long> parseOrderMap(String str) {
		try {
			Map<Long, Long> result = new LinkedHashMap<>();
			if (StringUtils.isNotBlank(str)) {
				String[] kvs = str.split("[,\\{\\}]");
				for (String kv : kvs) {
					if (StringUtils.isNotBlank(kv)) {
						String[] kvArray = kv.split(":");
						result.put(Long.valueOf(kvArray[0]), Long.valueOf(kvArray[1]));
					}
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException("can not parse leader info from giving string", e);
		}
	}

	@Override
	public DUser updateUser(DUser dUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DUser createOrUpdate(DUser dUser) {
		// TODO Auto-generated method stub
		return null;
	}
}
