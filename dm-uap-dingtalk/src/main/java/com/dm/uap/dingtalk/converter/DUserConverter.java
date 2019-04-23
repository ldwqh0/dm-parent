package com.dm.uap.dingtalk.converter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.dm.dingtalk.api.response.OapiUserGetResponse;
import com.dm.uap.dingtalk.entity.DUser;

@Component
public class DUserConverter {

	public void copyProperties(DUser dUser, OapiUserGetResponse rsp) {
		dUser.setActive(rsp.getActive());
		dUser.setAdmin(rsp.getIsAdmin());
		dUser.setAvatar(rsp.getAvatar());
		dUser.setBoss(rsp.getIsBoss());
//dUser.setDepartments(departments);
		dUser.setEmail(rsp.getEmail());
		dUser.setHide(rsp.getIsHide());
		if (!Objects.isNull(rsp.getHiredDate())) {
			dUser.setHiredDate(ZonedDateTime.ofInstant(rsp.getHiredDate().toInstant(), ZoneId.systemDefault()));
		}
//		dUser.setIsLeaderInDepts(isLeaderInDepts);
		dUser.setJobnumber(rsp.getJobnumber());
		dUser.setMobile(rsp.getMobile());
		dUser.setName(rsp.getName());
//		dUser.setOrderInDepts();
		dUser.setOrgEmail(rsp.getOrgEmail());
		dUser.setPosition(rsp.getPosition());
		dUser.setRemark(rsp.getRemark());
		dUser.setSenior(rsp.getIsSenior());
		dUser.setStateCode(rsp.getStateCode());
		dUser.setTel(rsp.getTel());
		dUser.setUnionid(rsp.getUnionid());
		dUser.setUserid(rsp.getUserid());
		dUser.setWorkPlace(rsp.getWorkPlace());
	}
}
