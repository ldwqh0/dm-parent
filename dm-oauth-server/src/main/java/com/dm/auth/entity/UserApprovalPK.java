package com.dm.auth.entity;

import java.io.Serializable;

public class UserApprovalPK implements Serializable {
	private static final long serialVersionUID = 6144610336580018926L;

	private String userId;

	private String clientId;

	private String scope;

	public UserApprovalPK(String userId, String clientId, String scope) {
		super();
		this.userId = userId;
		this.clientId = clientId;
		this.scope = scope;
	}

	public UserApprovalPK() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserApprovalPK other = (UserApprovalPK) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
