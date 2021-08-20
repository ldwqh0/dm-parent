package com.dm.server.authorization.service.impl

import com.dm.common.exception.DataNotExistException
import com.dm.server.authorization.entity.Client
import com.dm.server.authorization.entity.UserApprovalResult
import com.dm.server.authorization.repository.ClientRepository
import com.dm.server.authorization.repository.UserApprovalResultRepository
import com.dm.uap.entity.User
import com.dm.uap.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.xyyh.oidc.core.ApprovalResult
import org.xyyh.oidc.core.ApprovalResultStore
import java.util.*
import javax.persistence.EntityManager


@Service
class UserApprovalStoreServiceImpl(
    private val approvalResultRepository: UserApprovalResultRepository,
    private val userRepository: UserRepository,
    private val clientRepository: ClientRepository,
    private val entityManager: EntityManager
) : ApprovalResultStore {

    @Transactional
    override fun save(username: String, clientId: String, result: ApprovalResult) {
        fun createResult(): UserApprovalResult {
            val user: User =
                userRepository.findOneByUsernameIgnoreCase(username).orElseThrow { DataNotExistException() }
            val client: Client = clientRepository.findByIdOrNull(clientId) ?: throw DataNotExistException()
            return UserApprovalResult(user, client)
        }

        val uar: UserApprovalResult = approvalResultRepository.findById(username, clientId) ?: createResult()
        uar.expireAt = result.expireAt
        uar.scopes = result.scopes
        uar.redirectUris = uar.redirectUris union result.redirectUris
        // Spring data jpa对于持久化这种类型的数据会有bug,调用原生的entityManager
        entityManager.persist(uar)
    }

    override fun get(userid: String, clientId: String): Optional<ApprovalResult> {
        val r = approvalResultRepository.findById(userid, clientId)
            ?.let { ApprovalResult.of(it.scopes, it.redirectUris, it.expireAt) }
        return Optional.ofNullable(r)
    }

    @Transactional
    override fun delete(userid: String, clientId: String) {
        userRepository.findOneByUsernameIgnoreCase(userid).flatMap {
            approvalResultRepository.findById(
                UserApprovalResult.Pk(
                    it, clientRepository.getById(clientId)
                )
            )
        }.ifPresent { entityManager.remove(it) }
    }
}
