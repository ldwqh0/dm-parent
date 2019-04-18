package com.dm.uap.dingtalk.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.dingding.model.response.OapiRoleListResponse.OpenRole;
import com.dm.dingding.model.response.OapiRoleListResponse.OpenRoleGroup;
import com.dm.dingding.service.DingTalkService;
import com.dm.uap.dingtalk.converter.DRoleConverter;
import com.dm.uap.dingtalk.converter.DRoleGroupConverter;
import com.dm.uap.dingtalk.entity.DRole;
import com.dm.uap.dingtalk.entity.DRoleGroup;
import com.dm.uap.dingtalk.repository.DRoleGroupRepository;
import com.dm.uap.dingtalk.repository.DRoleRepository;
import com.dm.uap.dingtalk.service.DRoleGroupService;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.Role.Status;
import com.dm.uap.repository.RoleGroupRepository;
import com.dm.uap.entity.RoleGroup;

@Service
public class DRoleGroupServiceImpl implements DRoleGroupService {

	@Autowired
	private DingTalkService dingTalkService;

	@Autowired
	private DRoleGroupConverter dRoleGroupConverter;

	@Autowired
	private DRoleGroupRepository dRoleGroupRepository;

	@Autowired
	private DRoleConverter dRoleConverter;

	@Autowired
	private DRoleRepository dRoleRepository;

	@Autowired
	private RoleGroupRepository uRoleGroupRepository;

	/**
	 * 从钉钉加载角色和角色组数据，并保存在本地
	 * 
	 * @return
	 */
	private List<DRoleGroup> fetchRoleGroup() {
		List<OpenRoleGroup> roleGroups = dingTalkService.fetchRoleGroups();
		List<Long> roleGroupIds = roleGroups.stream().map(OpenRoleGroup::getGroupId).collect(Collectors.toList());
		List<Long> roleIds = roleGroups.stream().map(OpenRoleGroup::getRoles)
				.flatMap(List::stream).map(OpenRole::getId).collect(Collectors.toList());
		dRoleRepository.deleteByIdNotIn(roleIds);
		dRoleGroupRepository.deleteByIdNotIn(roleGroupIds);

		List<DRoleGroup> result = roleGroups.stream().map(oGroup -> {
			Long groupId = oGroup.getGroupId();
			final DRoleGroup dRoleGroup = dRoleGroupRepository.existsById(groupId)
					? dRoleGroupRepository.getOne(groupId)
					: new DRoleGroup();
			dRoleGroupConverter.copyProperties(dRoleGroup, oGroup);
			List<OpenRole> oRoles = oGroup.getRoles();
			if (CollectionUtils.isNotEmpty(oRoles)) {
				Set<DRole> dRoles = oRoles.stream().map(oRole -> {
					Long oRoleId = oRole.getId();
					DRole dRole = dRoleRepository.existsById(oRoleId) ? dRoleRepository.getOne(oRoleId) : new DRole();
					dRoleConverter.copyProperties(dRole, oRole);
					return dRole;
				}).collect(Collectors.toSet());
				dRoleGroup.setRoles(dRoles);
			}
			return dRoleGroup;
		}).collect(Collectors.toList());
		return dRoleGroupRepository.saveAll(result);
	}

	/**
	 * 同步本地角色信息到系统角色
	 * 
	 * @param dRoleGroups
	 * @return
	 */
	public List<RoleGroup> syncLocalToUap(List<DRoleGroup> dRoleGroups) {
		List<RoleGroup> results = dRoleGroups.stream().map(dRoleGroup -> {
			final RoleGroup uRoleGroup = Objects.isNull(dRoleGroup.getGroup()) ? new RoleGroup()
					: dRoleGroup.getGroup();
			uRoleGroup.setName(dRoleGroup.getName());
			dRoleGroup.setGroup(uRoleGroup);
			Set<Role> roles = dRoleGroup.getRoles().stream().map(dRole -> {
				Role uRole = dRole.getRole();
				if (Objects.isNull(uRole)) {
					uRole = new Role();
				}
				uRole.setName(dRole.getName());
				uRole.setState(Status.ENABLED);
				uRole.setGroup(uRoleGroup);
				dRole.setRole(uRole);
				return uRole;
			}).collect(Collectors.toSet());
			uRoleGroup.setRoles(roles);
			return uRoleGroup;
		}).collect(Collectors.toList());
		return uRoleGroupRepository.saveAll(results);
	}

	@Override
	@Async
	@Transactional
	public void syncToUap() {
		syncLocalToUap(fetchRoleGroup());
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		dRoleGroupRepository.deleteById(id);
	}
}
