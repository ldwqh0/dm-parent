package com.dm.region.dto;

import com.dm.common.validation.groups.ReferenceGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public RegionDto getParent() {
        return parent;
    }

    public void setParent(RegionDto parent) {
        this.parent = parent;
    }
}
