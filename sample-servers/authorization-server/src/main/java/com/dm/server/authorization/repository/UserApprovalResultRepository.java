package com.dm.server.authorization.repository;

import com.dm.server.authorization.entity.UserApprovalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 *
 * <p>UserApprovalResultRepository</p>
 *
 * @author ldwqh0@outlook.com
 */
public interface UserApprovalResultRepository extends JpaRepository<UserApprovalResult, UserApprovalResult.Pk> {

    @Query("select uar from UserApprovalResult uar where lower(uar.user.username)=lower(:username) and lower(uar.client.id)=lower(:clientId)")
    Optional<UserApprovalResult> findById(@Param("username") String username, @Param("clientId") String clientId);

}
