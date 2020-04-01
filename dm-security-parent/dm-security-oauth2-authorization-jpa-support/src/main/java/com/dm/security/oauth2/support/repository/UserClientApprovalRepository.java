package com.dm.security.oauth2.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dm.security.oauth2.support.entity.UserClientApproval;
import com.dm.security.oauth2.support.entity.UserClientApprovalPK;

public interface UserClientApprovalRepository extends JpaRepository<UserClientApproval, UserClientApprovalPK> {

    public List<UserClientApproval> findByClientIdAndUserId(String clientId, String userId);

    @Query("DELETE FROM UserClientApproval uca WHERE uca.clientId=?1")
    @Modifying
    public int deleteByClientId(String clientId);

    @Query("DELETE FROM UserClientApproval uca WHERE uca.userId=?1")
    public int deleteByUserId(String userid);

    public default void deleteById(String client, String userid, String scope) {
        deleteById(new UserClientApprovalPK(userid, client, scope));
    }

}
