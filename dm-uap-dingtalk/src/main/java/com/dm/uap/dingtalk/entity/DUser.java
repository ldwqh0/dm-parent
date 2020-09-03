package com.dm.uap.dingtalk.entity;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.MapKeyJoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.dm.uap.entity.User;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@Table(name = "dd_user_", indexes = {
        @Index(columnList = "deleted_", name = "idx_dd_user_deleted_"),
        @Index(columnList = "corp_id_,userid_", name = "uk_dd_user_corpid_userid_")
})
@IdClass(DUserId.class)
public class DUser {

    // 一个用户在一个企业中只能出现一次

    /**
     * 用户所属企业的ID
     */
    @Id
    @Column(name = "corp_id_")
    private String corpId;

    /**
     * 用户的全局唯一识别符，用于一个用户在不同的企业确定用户的统一性
     */
    @Id
    @Column(name = "unionid_")
    private String unionid;

    /**
     * 用户在企业内的id
     */
    @Column(name = "userid_")
    private String userid;

    @Column(name = "name_")
    private String name;

    @Column(name = "tel_")
    private String tel;

    @Column(name = "work_place_")
    private String workPlace;

    @Column(name = "remark_")
    private String remark;

    @Column(name = "mobile_")
    private String mobile;

    @Column(name = "email_")
    private String email;

    @Column(name = "org_email_")
    private String orgEmail;

    @Column(name = "active_")
    private Boolean active;

    @Column(name = "order_")
    @JoinTable(name = "dd_department_user_order_", joinColumns = {
            @JoinColumn(name = "dd_user_unionid_", referencedColumnName = "unionid_"),
            @JoinColumn(name = "dd_user_corp_id_", referencedColumnName = "corp_id_")
    })
    @ElementCollection
    @MapKeyJoinColumns({
            @MapKeyJoinColumn(name = "dd_department_id_", referencedColumnName = "id_"),
            @MapKeyJoinColumn(name = "dd_department_corp_id_", referencedColumnName = "corp_id_")
    })
    private Map<DDepartment, Long> orderInDepts;

    @Column(name = "is_admin_")
    private Boolean admin;

    @Column(name = "is_boss_")
    private Boolean boss;

    // 标识用户是否被删除
    @Column(name = "deleted_")
    private Boolean deleted = false;

    @Column(name = "is_leader_")
    @ElementCollection
    @JoinTable(name = "dd_department_user_leader_", joinColumns = {
            @JoinColumn(name = "dd_user_unionid_", referencedColumnName = "unionid_"),
            @JoinColumn(name = "dd_user_corp_id_", referencedColumnName = "corp_id_")
    })
    @MapKeyJoinColumns({
            @MapKeyJoinColumn(name = "dd_department_id_", referencedColumnName = "id_"),
            @MapKeyJoinColumn(name = "dd_department_corp_id_", referencedColumnName = "corp_id_")
    })
    private Map<DDepartment, Boolean> leaderInDepts;

    @Column(name = "hide_")
    private Boolean hide;

    @ManyToMany
    @JoinTable(name = "dd_department_dd_user_", joinColumns = {
            @JoinColumn(name = "dd_user_unionid_", referencedColumnName = "unionid_"),
            @JoinColumn(name = "dd_user_corp_id_", referencedColumnName = "corp_id_")
    }, inverseJoinColumns = {
            @JoinColumn(name = "dd_department_id_", referencedColumnName = "id_"),
            @JoinColumn(name = "dd_department_corp_id_", referencedColumnName = "corp_id_")
    })
    private Set<DDepartment> departments;

    @Column(name = "position_")
    private String position;

    @Column(name = "avatar_")
    private String avatar;

    @Column(name = "hired_date_")
    private ZonedDateTime hiredDate;

    @Column(name = "jobnumber_")
    private String jobnumber;

    @Column(name = "senior_")
    private Boolean senior;

    @Column(name = "state_code_")
    private String stateCode;

    @ManyToMany
    @JoinTable(name = "dd_role_user_", joinColumns = {
            @JoinColumn(name = "dd_user_unionid_", referencedColumnName = "unionid_"),
            @JoinColumn(name = "dd_user_corp_id_", referencedColumnName = "corp_id_")
    }, inverseJoinColumns = {
            @JoinColumn(name = "dd_role_id_", referencedColumnName = "id_"),
            @JoinColumn(name = "dd_role_corp_id_", referencedColumnName = "corp_id_")
    })
    private Set<DRole> roles;

    @OneToOne(cascade = { MERGE, PERSIST, REFRESH, DETACH })
    @JoinColumn(name = "dm_user_id_")
    private User user;

    /**
     * 这两个个字段仅仅在数据传输的过程中使用,用于{@link DUser}向{@link User}的传输，不会存储实体
     */
    @Transient
    private Map<DDepartment, String> posts;

    public void setPosts(Map<DDepartment, String> posts) {
        this.posts = posts;
        Set<Entry<DDepartment, String>> postEntry = posts.entrySet();
        String pos = null;
        Set<DDepartment> departments = new HashSet<DDepartment>();
        for (Entry<DDepartment, String> entry : postEntry) {
            if (StringUtils.isBlank(pos)) {
                pos = entry.getValue();
            }
            departments.add(entry.getKey());
        }
        this.position = pos;
        this.departments = departments;
    }

    DUser() {
    }

    private void setCorpId(String corpid) {
        this.corpId = corpid;
    }

    private void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public DUser(String corpid, String unionid) {
        setCorpId(corpid);
        setUnionid(unionid);
    }

}
