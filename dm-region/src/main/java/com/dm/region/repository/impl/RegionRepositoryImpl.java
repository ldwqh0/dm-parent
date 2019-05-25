package com.dm.region.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dm.region.entity.QRegion;
import com.dm.region.entity.Region;
import com.querydsl.jpa.impl.JPAQuery;

public class RegionRepositoryImpl {

	@Autowired
	private EntityManager em;

	private final QRegion qRegion = QRegion.region;

	public List<Region> findAllChildren(String code) {
		JPAQuery<Region> query = new JPAQuery<>(em);
		List<Region> result = new ArrayList<Region>();
		List<Region> tmp = null;
		query.select(qRegion).from(qRegion)
				.where(qRegion.parentCode.code.eq(code));
		tmp = query.fetch();
		while (CollectionUtils.isNotEmpty(tmp)) {
			result.addAll(tmp);
			tmp = findAllChildren(tmp);
		}
		result.addAll(query.fetch());
		return result;
	}

	private List<Region> findAllChildren(List<Region> parents) {
		JPAQuery<Region> query = new JPAQuery<>(em);
		query.select(qRegion).from(qRegion).where(qRegion.parentCode.in(parents));
		return query.fetch();
	}
}
