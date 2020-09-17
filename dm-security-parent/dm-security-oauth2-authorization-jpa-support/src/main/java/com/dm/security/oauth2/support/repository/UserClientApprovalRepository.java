package com.dm.security.oauth2.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dm.security.oauth2.support.entity.UserClientApproval;
import com.dm.security.oauth2.support.entity.UserClientApprovalPK;
import org.springframework.data.repository.query.Param;

public interface UserClientApprovalRepository extends JpaRepository<UserClientApproval, UserClientApprovalPK> {

    List<UserClientApproval> findByClientIdAndUserId(String clientId, String userId);

    @Query("DELETE FROM UserClientApproval uca WHERE uca.clientId=:clientId")
    @Modifying
    int deleteByClientId(@Param("clientId") String clientId);

    @Query("DELETE FROM UserClientApproval uca WHERE uca.userId=:userid")
    int deleteByUserId(@Param("userid") String userid);

    default void deleteById(String client, String userid, String scope) {
        deleteById(new UserClientApprovalPK(userid, client, scope));
    }

}
