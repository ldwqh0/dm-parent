package com.dm.dingtalk.api.response;

/**
 * TOP DingTalk-API: dingtalk.oapi.user.getuserinfo response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class OapiUserGetuserinfoResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1559732755389923482L;

	/**
	 * deviceId
	 */
	private String deviceId;

	/**
	 * errcode
	 */
	private Long errcode;

	/**
	 * errmsg
	 */
	private String errmsg;

	/**
	 * is_sys
	 */
	private Boolean isSys;

	/**
	 * sys_level
	 */
	private String sysLevel;

	/**
	 * userid
	 */
	private String userid;

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setErrcode(Long errcode) {
		this.errcode = errcode;
	}

	public Long getErrcode() {
		return this.errcode;
	}

	@Override
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Override
	public String getErrmsg() {
		return this.errmsg;
	}

	public void setIsSys(Boolean isSys) {
		this.isSys = isSys;
	}

	public Boolean getIsSys() {
		return this.isSys;
	}

	public void setSysLevel(String sysLevel) {
		this.sysLevel = sysLevel;
	}

	public String getSysLevel() {
		return this.sysLevel;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return this.userid;
	}

	@Override
	public boolean isSuccess() {
		return getErrcode() == null || getErrcode().equals(0L);
	}

}
