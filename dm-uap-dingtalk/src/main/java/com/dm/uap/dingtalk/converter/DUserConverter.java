package com.dm.uap.dingtalk.converter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.dm.dingtalk.api.request.OapiUserCreateRequest;
import com.dm.dingtalk.api.request.OapiUserUpdateRequest;
import com.dm.dingtalk.api.response.OapiUserGetResponse;
import com.dm.uap.dingtalk.entity.DDepartment;
import com.dm.uap.dingtalk.entity.DUser;
import com.dm.uap.entity.User;

@Component
public class DUserConverter {

	public void copyProperties(DUser dUser, OapiUserGetResponse rsp) {
		dUser.setActive(rsp.getActive());
		dUser.setAdmin(rsp.getIsAdmin());
		dUser.setAvatar(rsp.getAvatar());
		dUser.setBoss(rsp.getIsBoss());
		// dUser.setDepartments(departments);
		dUser.setEmail(rsp.getEmail());
		dUser.setHide(rsp.getIsHide());
		if (!Objects.isNull(rsp.getHiredDate())) {
			dUser.setHiredDate(ZonedDateTime.ofInstant(rsp.getHiredDate().toInstant(), ZoneId.systemDefault()));
		}
		// dUser.setIsLeaderInDepts(isLeaderInDepts);
		dUser.setJobnumber(rsp.getJobnumber());
		dUser.setMobile(rsp.getMobile());
		dUser.setName(rsp.getName());
		// dUser.setOrderInDepts();
		dUser.setOrgEmail(rsp.getOrgEmail());
		dUser.setPosition(rsp.getPosition());
		dUser.setRemark(rsp.getRemark());
		dUser.setSenior(rsp.getIsSenior());
		dUser.setStateCode(rsp.getStateCode());
		dUser.setTel(rsp.getTel());
		dUser.setUnionid(rsp.getUnionid());

		dUser.setWorkPlace(rsp.getWorkPlace());
	}

	public void copyProperties(User user, DUser dUser) {
		user.setAccountExpired(Boolean.FALSE);
		user.setCredentialsExpired(Boolean.FALSE);
		user.setDescription(dUser.getRemark());
		user.setEmail(dUser.getEmail());
		user.setEnabled(Boolean.FALSE);
		user.setFullname(dUser.getName());
		user.setLocked(Boolean.FALSE);
		user.setMobile(dUser.getMobile());
		user.setRegionCode(dUser.getStateCode());
		user.setUsername(dUser.getUserid());
	}

	public OapiUserCreateRequest toOapiUserCreateRequest(DUser dUser) {
		OapiUserCreateRequest request = new OapiUserCreateRequest();
		// 这里仅仅设置用户的必要信息
		request.setMobile(dUser.getMobile());
		request.setName(dUser.getName());
		request.setUserid(dUser.getUserid());
		request.setPosition(dUser.getPosition());
		request.setEmail(dUser.getEmail());
		request.setJobnumber(dUser.getJobnumber());
		Set<DDepartment> dDepartments = dUser.getDepartments();
		if (CollectionUtils.isNotEmpty(dDepartments)) {
			List<Long> departments = dDepartments.stream().map(DDepartment::getId).collect(Collectors.toList());
			request.setDepartment(departments);
		}
		// TODO 还要设置orderindepts
		return request;
	}

	public OapiUserUpdateRequest toOapiUserUpdateRequest(DUser dUser) {
		OapiUserUpdateRequest request = new OapiUserUpdateRequest(dUser.getUserid());
		request.setMobile(dUser.getMobile());
		request.setName(dUser.getName());
		request.setPosition(dUser.getPosition());
		request.setRemark(dUser.getRemark());
		request.setEmail(dUser.getEmail());
		request.setJobnumber(dUser.getJobnumber());
		Set<DDepartment> dDepartments = dUser.getDepartments();
		if (CollectionUtils.isNotEmpty(dDepartments)) {
			List<Long> departments = dDepartments.stream().map(DDepartment::getId).collect(Collectors.toList());
			request.setDepartment(departments);
		}
		return request;
	}
}
