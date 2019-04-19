package com.dm.dingtalk.api.response;

import java.util.List;

/**
 * TOP DingTalk-API: dingtalk.oapi.department.list_ids response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class OapiDepartmentListIdsResponse extends TaobaoResponse {

	private static final long serialVersionUID = 7369121852635438152L;

	private Long errcode;

	private String errmsg;

	private List<Long> subDeptIdList;

	public void setErrcode(Long errcode) {
		this.errcode = errcode;
	}

	public Long getErrcode() {
		return this.errcode;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrmsg() {
		return this.errmsg;
	}

	public void setSubDeptIdList(List<Long> subDeptIdList) {
		this.subDeptIdList = subDeptIdList;
	}

	public List<Long> getSubDeptIdList() {
		return this.subDeptIdList;
	}

	@Override
	public boolean isSuccess() {
		return getErrcode() == null || getErrcode().equals(0L);
	}

}
