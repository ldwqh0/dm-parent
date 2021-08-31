package com.dm.uap.dto;


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

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
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
