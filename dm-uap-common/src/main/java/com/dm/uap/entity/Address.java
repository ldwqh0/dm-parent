package com.dm.uap.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class Address implements Serializable {

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

}
