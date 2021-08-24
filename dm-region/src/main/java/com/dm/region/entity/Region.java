package com.dm.region.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
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
    @Column(name = "code_", nullable = false, unique = true, length = 20)
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
    @JoinColumn(name = "parent_code_")
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
}
