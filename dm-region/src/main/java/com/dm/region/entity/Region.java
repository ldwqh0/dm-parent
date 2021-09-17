package com.dm.region.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dm_region_")
public class Region implements Serializable {
    private static final long serialVersionUID = 2116833927883554195L;

    public Region() {
    }

    public Region(String name) {
        this.name = name;
    }

    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Region(String code, String name, Region parentCode) {
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
    }

    /**
     * 编码
     */
    @Id
    @Column(name = "code_", nullable = false, length = 20)
    private String code;

    /**
     * 名称
     */
    @Column(name = "name_", length = 50)
    private String name;

    /**
     * 上级编码
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code_", foreignKey = @ForeignKey(name = "FK_dm_code_parent_"))
    private Region parentCode;

    @Column(name = "longitude_")
    private Double longitude;

    @Column(name = "latitude_")
    private Double latitude;

    @Column(name = "type_", length = 10)
    private String type;

    @Column(name = "url_")
    private String href;

    @Column(name = "synced_")
    private boolean synced = false;

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

    public Region getParentCode() {
        return parentCode;
    }

    public void setParentCode(Region parentCode) {
        this.parentCode = parentCode;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
