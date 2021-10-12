package com.dm.dingtalk.api.response;

import java.io.Serializable;
import java.util.Objects;

public class OpenRole implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7034570754033834272L;

    /**
     * 角色id
     */
    private Long id;

    private String corpId;

    /**
     * 角色组id
     */
    private Long groupId;
    /**
     * 角色名称
     */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenRole openRole = (OpenRole) o;
        return Objects.equals(id, openRole.id) && Objects.equals(corpId, openRole.corpId) && Objects.equals(groupId, openRole.groupId) && Objects.equals(name, openRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, corpId, groupId, name);
    }
}
