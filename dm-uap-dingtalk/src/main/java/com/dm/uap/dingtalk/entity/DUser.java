package com.dm.uap.dingtalk.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
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
@Table(name = "dd_user_", indexes = { @Index(columnList = "deleted_", name = "idx_dd_user_deleted_") })
public class DUser implements Serializable {

    private static final long serialVersionUID = -6763998745823230765L;
    @Id
    @Column(name = "userid_")
    private String userid;

    @Column(name = "unionid_")
    private String unionid;

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
            @JoinColumn(name = "dd_user_id_")
    })
    @ElementCollection
    @MapKeyJoinColumn(name = "dd_department_id_")
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
            @JoinColumn(name = "dd_user_id_")
    })
    @MapKeyJoinColumn(name = "dd_department_id_")
    private Map<DDepartment, Boolean> leaderInDepts;

    @Column(name = "hide_")
    private Boolean hide;

    @ManyToMany
    @JoinTable(name = "dd_department_dd_user_", joinColumns = {
            @JoinColumn(name = "dd_user_id_")
    }, inverseJoinColumns = {
            @JoinColumn(name = "dd_department_id_")
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
            @JoinColumn(name = "dd_user_id_")
    }, inverseJoinColumns = {
            @JoinColumn(name = "dd_role_id_")
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

    public DUser() {
        super();
    }

    public DUser(String userid) {
        super();
        this.userid = userid;
    }

    public void setUserid(String userid) {
        if (StringUtils.isBlank(this.userid)) {
            this.userid = userid;
        } else if (!Objects.equals(this.userid, userid)) {
            throw new UnsupportedOperationException("you can not change the userid");
        }
    }

}
