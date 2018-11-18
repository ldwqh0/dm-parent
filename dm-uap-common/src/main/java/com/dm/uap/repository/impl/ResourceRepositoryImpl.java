package com.dm.uap.repository.impl;

import java.util.Collections;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.dm.uap.entity.QResource;
import com.dm.uap.entity.Resource;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class ResourceRepositoryImpl {

	@Autowired
	private EntityManager em;

	private final QResource qResource = QResource.resource;

	public Page<Resource> find(String key, Pageable pageable) {
		JPAQuery<Resource> query = new JPAQuery<Resource>(em);
		query.select(qResource).from(qResource);
		if (StringUtils.isNotBlank(key)) {
			query.where(qResource.matcher.containsIgnoreCase(key).or(qResource.description.containsIgnoreCase(key)));
		}
		Long count = query.fetchCount();
		if (count > 0) {
			query.offset(pageable.getOffset()).limit(pageable.getPageSize());
			return new PageImpl<Resource>(query.fetch(), pageable, count);
		} else {
			return new PageImpl<Resource>(Collections.emptyList(), pageable, 0);
		}
	}
}
