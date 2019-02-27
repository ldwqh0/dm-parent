package com.dm.uap.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Embeddable
@Data
public class RegionInfo implements Serializable {

	private static final long serialVersionUID = -9096014486359089191L;

	@JoinColumn(name = "provincial_code_")
	@ManyToOne
	private Region provincial;

	@JoinColumn(name = "city_code_")
	@ManyToOne
	private Region city;

	@ManyToOne
	@JoinColumn(name = "county_code_")
	private Region county;

	public List<String> asList() {
		String provincialCode = null;
		String cityCode = null;
		String countryCode = null;

		if (!Objects.isNull(provincial)) {
			provincialCode = provincial.getCode();
		}
		if (!Objects.isNull(city)) {
			cityCode = city.getCode();
		}
		if (!Objects.isNull(county)) {
			countryCode = county.getCode();
		}
		return Arrays.asList(new String[] { provincialCode, cityCode, countryCode });
	}

	public RegionInfo() {
		super();
	}

}
