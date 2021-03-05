package com.dm.uap.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
/**
 * 表示用户的职务信息
 *
 * @author LiDong
 *
 */
public class UserPostDto implements Serializable {

    private static final long serialVersionUID = -1344418453725050901L;

    /**
     * 用户所在部门
     */
    @Valid
    @NotNull(groups = {UserDto.Default.class})
    private DepartmentDto department;

    /**
     * 用户在该部门所属的职务
     */
    @NotNull(groups = {UserDto.Default.class})
    private String post;

    public UserPostDto(DepartmentDto department, String post) {
        super();
        this.department = department;
        this.post = post;
    }

    public UserPostDto() {
        super();
    }

}
