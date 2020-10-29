package com.dm.uap.dto;

import com.dm.uap.entity.Role.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    private RoleGroupDto group;

    @Setter(onMethod_ = {@JsonProperty(access = Access.READ_ONLY)})
    @Getter(onMethod_ = {@JsonIgnoreProperties({"password", "roles"})})
    @Valid
    private List<UserDto> users;

}
