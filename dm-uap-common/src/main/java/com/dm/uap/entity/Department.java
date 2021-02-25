package com.dm.uap.entity;

import com.dm.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "dm_department_", uniqueConstraints = {
    @UniqueConstraint(name = "UK_dm_department_parent_id_full_name_", columnNames = {"parent_id_", "full_name_"})
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
}
