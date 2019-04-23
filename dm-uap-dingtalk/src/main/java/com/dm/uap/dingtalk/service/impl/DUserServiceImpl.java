package com.dm.uap.dingtalk.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dm.dingtalk.api.response.OapiUserGetResponse;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.uap.dingtalk.converter.DUserConverter;
import com.dm.uap.dingtalk.entity.DDepartment;
import com.dm.uap.dingtalk.entity.DUser;
import com.dm.uap.dingtalk.repository.DDepartmentRepository;
import com.dm.uap.dingtalk.repository.DUserRepository;
import com.dm.uap.dingtalk.service.DUserService;

@Service
public class DUserServiceImpl implements DUserService {

	@Autowired
	private DingTalkService dingTalkService;

	@Autowired
	private DDepartmentRepository ddepartmentRepository;

	@Autowired
	private DUserRepository ddUserRepository;

	@Autowired
	private DUserConverter dUserConverter;

	@Async
	@Transactional
	@Override
	public void sync() {
		Set<String> userIds = getUserIds();
		ddUserRepository.deleteByIdNotIn(userIds);
		for (String userId : userIds) {
			if (ddUserRepository.existsById(userId)) {
				update(userId, dingTalkService.fetchUserById(userId));
			} else {
				save(dingTalkService.fetchUserById(userId));
			}
		}
	}

	private Set<String> getUserIds() {
		List<DDepartment> dDepartments = ddepartmentRepository.findAll();
		return dDepartments.stream()
				.map(DDepartment::getId)
				.map(dingTalkService::fetchUsers)
				.map(OapiUserGetDeptMemberResponse::getUserIds)
				.flatMap(List::stream)
				.collect(Collectors.toSet());
	}

	private DUser update(String userId, OapiUserGetResponse rsp) {
		DUser dUser = ddUserRepository.getOne(userId);
		copyProperties(dUser, rsp);
		return ddUserRepository.save(dUser);
	}

	private DUser save(OapiUserGetResponse rsp) {
		DUser dUser = new DUser();
		copyProperties(dUser, rsp);
		return ddUserRepository.save(dUser);
	}

	private void copyProperties(DUser dUser, OapiUserGetResponse rsp) {
		dUserConverter.copyProperties(dUser, rsp);
		List<DDepartment> departments = rsp.getDepartment().stream().map(ddepartmentRepository::getOne)
				.collect(Collectors.toList());
		try {
			Map<Long, Boolean> isLeaderMap = parseLeaderMap(rsp.getIsLeaderInDepts());
			if (MapUtils.isNotEmpty(isLeaderMap)) {
				Map<DDepartment, Boolean> dLeaderMap = new HashMap<>();
				for (Entry<Long, Boolean> entry : isLeaderMap.entrySet()) {
					dLeaderMap.put(ddepartmentRepository.getOne(entry.getKey()), entry.getValue());
				}
				dUser.setLeaderInDepts(dLeaderMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<Long, Long> orderMap = parseOrderMap(rsp.getOrderInDepts());
		if (MapUtils.isNotEmpty(orderMap)) {
			Map<DDepartment, Long> dOrderMap = new HashMap<DDepartment, Long>();
			for (Entry<Long, Long> orderEntry : orderMap.entrySet()) {
				dOrderMap.put(ddepartmentRepository.getOne(orderEntry.getKey()), orderEntry.getValue());
			}
			dUser.setOrderInDepts(dOrderMap);
		}

		dUser.setDepartments(departments);
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
}
