package com.dm.uap.entity;

import com.dm.common.entity.AbstractEntity;

import javax.persistence.*;
import java.util.Optional;

@Entity


@Table(name = "dm_department_", uniqueConstraints = {
    @UniqueConstraint(name = "uk_dm_department_parent_id_full_name_", columnNames = {"parent_id_", "full_name_"})
})
public class Department extends AbstractEntity {

    /**
     * 部门类型
     *
     * @author LiDong
     */
    public enum Types {
        /**
         * 机构
         */
        ORGANS,
        /**
         * 部门
         */
        DEPARTMENT,
        /**
         * 分组
         */
        GROUP
    }

    /**
     * 部门名称
     */
    @Column(name = "full_name_", length = 100)
    private String fullname;

    /**
     * 短名称
     */
    @Column(name = "short_name_", length = 100)
    private String shortname;

    /**
     * 上级部门
     */
    @ManyToOne
    @JoinColumn(name = "parent_id_", foreignKey = @ForeignKey(name = "FK_dm_department_parent_id_"))
    private Department parent;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_")
    private Types type;

    /**
     * 部门描述
     */
    @Column(name = "description_", length = 2000)
    private String description;

    @Column(name = "order_")
    private Long order;

    /**
     * 部门的logo,可能是文件的ID,路径，或者文件的base64编码
     */
    @Column(name = "logo_")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String logo;


    /**
     * 部门主管
     */
    @ManyToOne
    @JoinColumn(name = "department_director_")
    private User director;

    public Optional<User> getDirector() {
        return Optional.ofNullable(director);
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Department getParent() {
        return parent;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setDirector(User director) {
        this.director = director;
    }
}
