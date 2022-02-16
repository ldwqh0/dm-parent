package com.dm.region.dto;

import com.dm.common.validation.groups.ReferenceGroup;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class RegionDto implements Serializable {

    private static final long serialVersionUID = -8378328574545247274L;

    /**
     * 编码
     */
    @NotEmpty
    @NotEmpty(groups = ReferenceGroup.class)
    private final String code;

    /**
     * 名称
     */
    @NotEmpty
    private final String name;

    /**
     * 经度
     */
    private final Double longitude;
    /**
     * 纬度
     */
    private final Double latitude;

    /**
     * 上级编码
     */
    @JsonIgnoreProperties("parent")
    private final RegionDto parent;

    @JsonCreator
    public RegionDto(@JsonProperty("code") String code,
                     @JsonProperty("name") String name,
                     @JsonProperty("longitude") Double longitude,
                     @JsonProperty("latitude") Double latitude,
                     @JsonProperty("parent") RegionDto parent) {
        this.code = code;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        if (Objects.isNull(parent)) {
            this.parent = null;
        } else {
            this.parent = new RegionDto(parent.code, parent.name, parent.longitude, parent.latitude);
        }
    }

    public RegionDto(String code, String name, Double longitude, Double latitude) {
        this(code, name, longitude, latitude, null);
    }

    public RegionDto(String code, String name) {
        this(code, name, null, null);
    }

    public RegionDto(String code) {
        this(code, null);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public RegionDto getParent() {
        return new RegionDto(parent.code, parent.name, parent.longitude, parent.latitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionDto regionDto = (RegionDto) o;
        return Objects.equals(code, regionDto.code) && Objects.equals(name, regionDto.name) && Objects.equals(longitude, regionDto.longitude) && Objects.equals(latitude, regionDto.latitude) && Objects.equals(parent, regionDto.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, longitude, latitude, parent);
    }

}
