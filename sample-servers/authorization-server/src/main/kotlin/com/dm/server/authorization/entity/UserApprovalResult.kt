package com.dm.server.authorization.entity

import com.dm.uap.entity.User
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.*


@Entity
@Table(
    name = "dm_approval_result_", indexes = [
        Index(name = "IDX_dm_approval_result_user_id_", columnList = "user_id_"),
        Index(name = "IDX_dm_approval_result_client_id_", columnList = "client_id_")
    ]
)
@IdClass(UserApprovalResult.Pk::class)
class UserApprovalResult(
    user: User? = null,
    client: Client? = null,
    @Column(name = "expire_at_")
    var expireAt: ZonedDateTime = ZonedDateTime.now(),
    @CollectionTable(
        name = "dm_approval_result_scope_",
        joinColumns = [JoinColumn(name = "user_id_", referencedColumnName = "user_id_"), JoinColumn(
            name = "client_id_",
            referencedColumnName = "client_id_"
        )],
        foreignKey = ForeignKey(name = "FK_dm_approval_result_scope_dm_approval_result_")
    )
    @ElementCollection
    @Column(name = "scope_", nullable = false, length = 50)
    var scopes: Set<String> = setOf(),

    @ElementCollection
    @CollectionTable(
        name = "dm_approval_result_redirect_uri_",
        joinColumns = [JoinColumn(name = "user_id_", referencedColumnName = "user_id_"), JoinColumn(
            name = "client_id_",
            referencedColumnName = "client_id_"
        )],
        foreignKey = ForeignKey(name = "FK_dm_approval_result_redirect_uri_dm_approval_result_")
    )
    @Column(name = "redirect_uri_", nullable = false, length = 400)
    var redirectUris: Set<String> = setOf()
) {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_", foreignKey = ForeignKey(name = "FK_dm_approval_result_user_id_"))
    var user: User? = user
        private set

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id_", foreignKey = ForeignKey(name = "FK_dm_approval_result_client_id_"))
    var client: Client? = client
        private set

    data class Pk(
        val user: User? = null,
        val client: Client? = null
    ) : Serializable
}
