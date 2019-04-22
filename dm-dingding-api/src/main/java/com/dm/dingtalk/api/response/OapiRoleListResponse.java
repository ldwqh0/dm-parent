package com.dm.dingtalk.api.response;

import java.io.Serializable;
import java.util.List;

/**
 * TOP DingTalk-API: dingtalk.oapi.role.list response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class OapiRoleListResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2829222498139478487L;

	/**
	 * errcode
	 */
	private Long errcode;

	/**
	 * errmsg
	 */
	private String errmsg;

	/**
	 * result
	 */
	private PageVo<OpenRoleGroup> result;

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

	public void setResult(PageVo<OpenRoleGroup> result) {
		this.result = result;
	}

	public PageVo<OpenRoleGroup> getResult() {
		return this.result;
	}

	@Override
	public boolean isSuccess() {
		return getErrcode() == null || getErrcode().equals(0L);
	}

	/**
	 * roles
	 *
	 * @author top auto create
	 * @since 1.0, null
	 */
	public static class OpenRole implements Serializable {
		private static final long serialVersionUID = 3158828343894518282L;
		/**
		 * 角色id
		 */
		private Long id;
		/**
		 * 角色名称
		 */
		private String name;

		public Long getId() {
			return this.id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * list
	 *
	 * @author top auto create
	 * @since 1.0, null
	 */
	public static class OpenRoleGroup implements Serializable {
		private static final long serialVersionUID = 5231897893516396395L;
		/**
		 * 角色组id
		 */
		private Long groupId;
		/**
		 * 角色组名称
		 */
		private String name;
		/**
		 * roles
		 */
		private List<OpenRole> roles;

		public Long getGroupId() {
			return this.groupId;
		}

		public void setGroupId(Long groupId) {
			this.groupId = groupId;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<OpenRole> getRoles() {
			return this.roles;
		}

		public void setRoles(List<OpenRole> roles) {
			this.roles = roles;
		}
	}

}
