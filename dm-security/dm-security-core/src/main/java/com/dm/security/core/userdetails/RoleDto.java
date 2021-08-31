//package com.dm.security.core.userdetails;
//
//import com.fasterxml.jackson.annotation.JsonGetter;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//public class RoleDto implements Serializable, GrantedAuthority {
//
//    private static final long serialVersionUID = -2204000372762540931L;
//    /**
//     * 角色id
//     */
//    private Long id;
//
//    /**
//     * 角色组
//     */
//    private String group;
//
//    /**
//     * 角色名称
//     */
//    private String name;
//
//    /**
//     * 角色信息，用户安全认证
//     *
//     * @return authority全称
//     * @ignore 这个属性不给前端提供
//     */
//    @Override
//    @JsonGetter
//    public String getAuthority() {
//        return group + "_" + name;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        RoleDto roleDto = (RoleDto) o;
//        return Objects.equals(id, roleDto.id) && Objects.equals(group, roleDto.group) && Objects.equals(name, roleDto.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, group, name);
//    }
//}
