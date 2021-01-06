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

    public interface ReferenceBy {
    }

    public interface New extends Default {
    }

    public interface Update extends Default {
    }

    public interface Default {

    }

    /**
     *
     */
    @NotNull(groups = ReferenceBy.class)
    private Long id;

    /**
     * 任何时候，角色名称不能为空
     */
    @NotBlank(groups = {New.class, Update.class})
    @Size(max = 100, groups = {Default.class})
    private String name;

    @Size(max = 2000, groups = {Default.class})
    private String description;

    @NotNull(groups = {New.class, Update.class})
    private Status state;

    @Valid
    @NotNull(groups = {New.class, Update.class})
    private String group;

    public String getFullname() {
        return group + "_" + name;
    }

    public void setFullname(String fullname) {
        String[] groupName = fullname.split("\\_", 2);
        group = groupName[0];
        name = groupName[1];
    }

}
