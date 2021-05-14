package com.dm.server.authorization.repository

import com.dm.server.authorization.entity.UserApprovalResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface UserApprovalResultRepository : JpaRepository<UserApprovalResult, UserApprovalResult.Pk> {
    @Query("select uar from UserApprovalResult uar where lower(uar.user.username)=lower(:username) and lower(uar.client.id)=lower(:clientId)")
    fun findById(
        @Param("username") username: String,
        @Param("clientId") clientId: String
    ): UserApprovalResult?
}
