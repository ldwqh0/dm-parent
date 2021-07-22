package com.dm.auth.dto;

import com.dm.auth.entity.Role.Status;
import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 角色数据结构
 *
 * @author LiDong
 */
@Data
@JsonInclude(value = Include.NON_EMPTY)
public class RoleDto implements IdentifiableDto<Long>, Serializable {
    private static final long serialVersionUID = 4725729366179649819L;

    public RoleDto() {
    }

    public RoleDto(String name, String group, String description) {
        this.name = name;
        this.group = group;
        this.description = description;
    }

    public interface ReferenceBy {
    }

    public interface New extends Default {
    }

    public interface Update extends Default {
    }

    public interface Default {

    }

    /**
     * 角色ID
     */
    @NotNull(groups = ReferenceBy.class)
    private Long id;

    /**
     * 角色名称
     *
     * @apiNote 任何时候，角色名称不能为空
     */
    @NotBlank(groups = {New.class, Update.class})
    @Size(max = 100, groups = {Default.class})
    private String name;

    /**
     * 角色描述
     */
    @Size(max = 2000, groups = {Default.class})
    private String description;

    /**
     * 角色状态
     */
    @NotNull(groups = {New.class, Update.class})
    private Status state = Status.ENABLED;

    /**
     * 角色所属组
     */
    @Valid
    @NotNull(groups = {New.class, Update.class})
    private String group;

    /**
     * 角色全称
     *
     * @return 获取角色的全程
     * @apiNote 角色的全称是 {group}_{name}
     */
    public String getFullname() {
        return group + "_" + name;
    }

}
