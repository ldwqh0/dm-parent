package com.dm.uap.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 表示用户的职务信息
 *
 * @author LiDong
 */
public class UserPostDto implements Serializable {

    private static final long serialVersionUID = -1344418453725050901L;

    /**
     * 用户所在部门
     */
    @Valid
    @NotNull(groups = {UserDto.Default.class})
    private final DepartmentDto department;

    /**
     * 用户在该部门所属的职务
     */
    @NotNull(groups = {UserDto.Default.class})
    private final String post;

    @JsonCreator
    public UserPostDto(@JsonProperty("department") DepartmentDto department,
                       @JsonProperty("post") String post) {
        this.department = department;
        this.post = post;
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public String getPost() {
        return post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPostDto that = (UserPostDto) o;
        return Objects.equals(department, that.department) && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, post);
    }
}
