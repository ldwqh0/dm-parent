package com.dm.dingtalk.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends DingTalkResponse {
	private String unionid;
	private String remark;
	private String userid;
//	private Map<Long, Boolean> isLeaderInDepts;

	@JsonProperty(value = "isLeaderInDepts")
	public void setLeaderInDepts(String obj) {
		System.out.println(obj);
	}

	public Map<Long, Boolean> getLeaderInDepts() {
		return null;
	}

}
