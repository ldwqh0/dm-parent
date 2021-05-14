package com.dm.server.authorization.entity

import com.dm.common.entity.Audit
import com.dm.common.entity.CreateAudit
import com.dm.common.entity.ModifyAudit
import org.springframework.data.domain.Auditable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "dm_client_",
    uniqueConstraints = [UniqueConstraint(name = "UK_dm_client_name_", columnNames = ["name_"])]
)
@EntityListeners(AuditingEntityListener::class)
class Client(
    @Id
    @Column(name = "id_", length = 36)
    private var id: String = UUID.randomUUID().toString(),

    @Column(name = "name_", length = 100, nullable = false, unique = true)
    var name: String = "",

    @Column(name = "secret_", nullable = false)
    var secret: String = UUID.randomUUID().toString(),

    @Column(name = "type_", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    var type: Type = Type.CLIENT,

    /**
     * token失效时间
     */
    @Column(name = "access_token_validity_seconds_", nullable = false)
    var accessTokenValiditySeconds: Int = 3600,

    /**
     * refreshToken失效时间
     */
    @Column(name = "refresh_token_validity_seconds_", nullable = false)
    var refreshTokenValiditySeconds: Int = 7200,

    @Column(name = "auto_approve_", nullable = false)
    var autoApprove: Boolean = false,

    @Column(name = "require_pkce_", nullable = false)
    var requirePkce: Boolean = false,

    @ElementCollection
    @Column(name = "authorized_grant_type_", nullable = false)
    @CollectionTable(
        name = "dm_client_authorized_grant_type_",
        joinColumns = [JoinColumn(
            name = "client_id_",
            foreignKey = ForeignKey(name = "FK_dm_client_authorized_grant_type_client_id_")
        )]
    )
    var authorizedGrantTypes: Set<String> = HashSet(),

    @ElementCollection
    @CollectionTable(
        name = "dm_client_scope_",
        joinColumns = [JoinColumn(
            name = "client_id_",
            foreignKey = ForeignKey(name = "FK_dm_client_scope_client_id_")
        )]
    )
    @Column(name = "scope_", nullable = false)
    var scopes: Set<String> = HashSet(),

    @ElementCollection
    @JoinTable(
        name = "dm_client_additional_information_",
        uniqueConstraints = [UniqueConstraint(
            name = "UK_dm_client_additional_information_client_id_key_value_",
            columnNames = ["client_id_", "key_", "value_"]
        )],
        joinColumns = [JoinColumn(
            name = "client_id_",
            foreignKey = ForeignKey(name = "FK_dm_client_additional_information_client_id_")
        )]
    )
    @MapKeyColumn(name = "key_")
    @Column(name = "value_", nullable = false)
    var additionalInformation: Map<String, String> = HashMap(),

    @ElementCollection
    @CollectionTable(
        name = "dm_client_registered_redirect_uri_",
        joinColumns = [JoinColumn(
            name = "client_id_",
            foreignKey = ForeignKey(name = "FK_dm_client_registered_redirect_uri_client_id_")
        )]
    )
    @Column(name = "registered_redirect_uri_", nullable = false)
    var registeredRedirectUris: Set<String> = HashSet(),

    @ElementCollection
    @CollectionTable(
        name = "dm_client_authority_",
        joinColumns = [JoinColumn(
            name = "client_id_",
            foreignKey = ForeignKey(name = "FK_dm_client_authority_client_id_")
        )]
    )
    @Column(name = "authority_", nullable = false)
    var authorities: Set<String> = HashSet(),

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "resource_id_", nullable = false)
    @CollectionTable(
        name = "dm_client_resource_id_",
        joinColumns = [JoinColumn(
            name = "client_id_",
            foreignKey = ForeignKey(name = "FK_dm_client_resource_id_client_id_")
        )]
    )
    var resourceIds: Set<String> = HashSet()

) : Auditable<Audit<Long, String>, String, ZonedDateTime> {

    private var createdBy: CreateAudit? = null

    private var createdDate: ZonedDateTime? = null

    private var lastModifiedBy: ModifyAudit? = null

    private var lastModifiedDate: ZonedDateTime? = null

    override fun isNew(): Boolean {
        return false
    }

    override fun getCreatedBy(): Optional<Audit<Long, String>> {
        return Optional.ofNullable(this.createdBy)
    }

    override fun setCreatedBy(createdBy: Audit<Long, String>) {
        this.createdBy = CreateAudit(createdBy)
    }

    override fun getCreatedDate(): Optional<ZonedDateTime> {
        return Optional.ofNullable(this.createdDate)
    }

    override fun setCreatedDate(creationDate: ZonedDateTime) {
        this.createdDate = creationDate
    }

    override fun getLastModifiedBy(): Optional<Audit<Long, String>> {
        return Optional.ofNullable(this.lastModifiedBy)
    }

    override fun setLastModifiedBy(lastModifiedBy: Audit<Long, String>) {
        this.lastModifiedBy = ModifyAudit(lastModifiedBy)
    }

    override fun getLastModifiedDate(): Optional<ZonedDateTime> {
        return Optional.ofNullable(this.lastModifiedDate)
    }

    override fun setLastModifiedDate(lastModifiedDate: ZonedDateTime) {
        this.lastModifiedDate = lastModifiedDate
    }

    override fun getId(): String {
        return this.id
    }

    enum class Type {
        CLIENT,
        RESOURCE
    }
}
