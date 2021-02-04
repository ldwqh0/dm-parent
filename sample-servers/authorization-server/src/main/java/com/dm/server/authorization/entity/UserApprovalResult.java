package com.dm.server.authorization.entity;

import com.dm.uap.entity.User;
import lombok.*;
import org.xyyh.authorization.core.ApprovalResult;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "dm_approval_result_", indexes = {
    @Index(name = "IDX_dm_approval_result_user_id_", columnList = "user_id_"),
    @Index(name = "IDX_dm_approval_result_client_id_", columnList = "client_id_")
})
@IdClass(UserApprovalResult.Pk.class)
public class UserApprovalResult implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_", foreignKey = @ForeignKey(name = "FK_dm_approval_result_user_id_"))
    @Id
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id_", foreignKey = @ForeignKey(name = "FK_dm_approval_result_client_id_"))
    @Id
    private Client client;

    @Column(name = "expire_at_")
    private ZonedDateTime expireAt;

    @CollectionTable(name = "dm_approval_result_scope_", joinColumns = {
        @JoinColumn(name = "user_id_", referencedColumnName = "user_id_"),
        @JoinColumn(name = "client_id_", referencedColumnName = "client_id_")
    }, foreignKey = @ForeignKey(name = "FK_dm_approval_result_scope_dm_approval_result_"))
    @ElementCollection
    @Column(name = "scope_", nullable = false, length = 50)
    private Set<String> scopes;

    @ElementCollection
    @CollectionTable(name = "dm_approval_result_redirect_uri_",joinColumns = {
        @JoinColumn(name = "user_id_", referencedColumnName = "user_id_"),
        @JoinColumn(name = "client_id_", referencedColumnName = "client_id_")
    },foreignKey = @ForeignKey(name = "FK_dm_approval_result_redirect_uri_dm_approval_result_"))
    @Column(name = "redirect_uri_", nullable = false, length = 400)
    private Set<String> redirectUris;


    public ApprovalResult toApprovalResult() {
        return ApprovalResult.of(scopes, redirectUris, expireAt);
    }

    @Data
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pk implements Serializable {
        private static final long serialVersionUID = -5299801925735248496L;

        private User user;

        private Client client;

    }
}
