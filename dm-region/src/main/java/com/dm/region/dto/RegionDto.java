package com.dm.region.dto;

import com.dm.common.validation.groups.ReferenceGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@JsonInclude(Include.NON_EMPTY)
public class RegionDto implements Serializable {

    private static final long serialVersionUID = -8378328574545247274L;

    /**
     * 编码
     */
    @NotEmpty
    @NotEmpty(groups = ReferenceGroup.class)
    private String code;

    /**
     * 名称
     */
    @NotEmpty
    private String name;

    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 上级编码
     */
    @JsonIgnoreProperties("parent")
    private RegionDto parent;

    public RegionDto() {

    }

    public RegionDto(String code, String name, RegionDto parentCode) {
        this.code = code;
        this.name = name;
        this.parent = parentCode;
    }

    public RegionDto(String name) {
        this.name = name;
    }

    public RegionDto(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
