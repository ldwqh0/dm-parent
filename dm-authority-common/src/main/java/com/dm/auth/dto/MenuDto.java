package com.dm.auth.dto;

import com.dm.auth.entity.Menu.MenuType;
import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

/**
 * 菜单结构
 *
 * @author LiDong
 */
@Data
@JsonInclude(Include.NON_ABSENT)
public class MenuDto implements Serializable, IdentifiableDto<Long> {
    private static final long serialVersionUID = 7184771144233410172L;
    private Long id;
    private String name;
    private String title;
    private Boolean enabled = Boolean.TRUE;
    private String url;
    private String icon;
    private String description;
    private MenuType type = MenuType.COMPONENT;
    @JsonIgnoreProperties(value = {"parent", "url", "description", "openInNewWindow"})
    private MenuDto parent;

    private Boolean openInNewWindow = Boolean.FALSE;

    public Optional<MenuDto> getParent() {
        return Optional.ofNullable(parent);
    }
}
