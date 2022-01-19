package com.dm.uap.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable, Cloneable {

    private static final long serialVersionUID = -1608137195031944938L;
    /**
     * Full mailing address, formatted for display or use on a mailing label. This field MAY contain multiple lines, separated by newlines. Newlines can be represented either as a carriage return/line feed pair ("\r\n") or as a single line feed character ("\n").
     */
    @Column(name = "formatted_", length = 20)
    private String formatted;

    /**
     * Full street address component, which MAY include house number, street name, Post Office Box, and multi-line extended street address information. This field MAY contain multiple lines, separated by newlines. Newlines can be represented either as a carriage return/line feed pair ("\r\n") or as a single line feed character ("\n").
     */
    @Column(name = "street_address_", length = 20)
    private String streetAddress;

    /**
     * City or locality component.
     */
    @Column(name = "locality_", length = 20)
    private String locality;

    /**
     * State, province, prefecture, or region component.
     */
    @Column(name = "region_", length = 20)
    private String region;

    /**
     * Zip code or postal code component.
     */
    @Column(name = "postal_code_", length = 20)
    private String postalCode;

    /**
     * Country name component .
     */
    @Column(name = "country_", length = 20)
    private String country;

    /**
     * 经度
     */
    @Column(name = "longitude_")
    private Double longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude_")
    private Double latitude;


    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
