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
//
//	public static RegionInfo fromList(List<String> region) {
//		if (CollectionUtils.isNotEmpty(region)) {
//			RegionInfo regionInfo = new RegionInfo();
//			int length = region.size();
//			if (CollectionUtils.isNotEmpty(region)) {
//				if (length > 0) {
//					regionInfo.provincialCode = region.get(0);
//				}
//				if (length > 1) {
//					regionInfo.cityCode = region.get(1);
//				}
//				if (length > 2) {
//					regionInfo.countyCode = region.get(2);
//				}
//			}
//			return regionInfo;
//		} else {
//			return null;
//		}
//
//	}

	public RegionInfo() {
		super();
	}

}
