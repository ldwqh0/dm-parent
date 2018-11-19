package com.dm.uap.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.collections4.CollectionUtils;

import lombok.Data;

@Embeddable
@Data
public class RegionInfo implements Serializable {

	private static final long serialVersionUID = -9096014486359089191L;

	@Column(name = "provincial_code_")
	private String provincialCode;

	@Column(name = "city_code_")
	private String cityCode;

	@Column(name = "county_code_")
	private String countyCode;

	public List<String> asList() {
		return Arrays.asList(new String[] { provincialCode, cityCode, countyCode });
	}

	public static RegionInfo fromList(List<String> region) {
		RegionInfo regionInfo = new RegionInfo();
		int length = region.size();
		if (CollectionUtils.isNotEmpty(region)) {
			if (length > 0) {
				regionInfo.provincialCode = region.get(0);
			}
			if (length > 1) {
				regionInfo.cityCode = region.get(1);
			}
			if (length > 2) {
				regionInfo.countyCode = region.get(2);
			}
		}
		return regionInfo;
	}

	public RegionInfo() {
		super();
	}

}
