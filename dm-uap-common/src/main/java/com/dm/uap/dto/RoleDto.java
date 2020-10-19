package com.dm.uap.dto;

import com.dm.common.validation.constraints.Mobile;
import com.dm.uap.entity.Role.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 角色数据结构
 *
 * @author LiDong
 */
@Data
@JsonInclude(value = Include.NON_EMPTY)
public class RoleDto implements Serializable {
    private static final long serialVersionUID = 4725729366179649819L;

    public interface ReferenceBy {
    }


    /**
     *
     */
    @NotNull(groups = ReferenceBy.class)
    private Long id;

    /**
     * 任何时候，角色名称不能为空
     */
    @NotBlank
    private String name;
    private String description;
    private Status state;

    @Valid
    @Mobile(groups = {UserDto.New.class, UserDto.Update.class})
    private RoleGroupDto group;

    @Setter(onMethod_ = {@JsonProperty(access = Access.READ_ONLY)})
    private List<UserDto> users;

    @JsonIgnoreProperties({"password", "roles"})
    public List<UserDto> getUsers() {
        return users;
    }

}
