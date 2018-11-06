package com.dm.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.auth.entity.UserApproval;
import com.dm.auth.entity.UserApprovalPK;

public interface UserApprovalRepository extends JpaRepository<UserApproval, UserApprovalPK> {

	public List<UserApproval> findByClientIdAndUserId(String clientId, String userId);

}
