package com.dm.region.repository.impl;

import com.dm.collections.CollectionUtils;
import com.dm.region.entity.QRegion;
import com.dm.region.entity.Region;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class RegionRepositoryImpl {

    private final EntityManager entityManager;

    private final QRegion qRegion = QRegion.region;

    public RegionRepositoryImpl(EntityManager em) {
        this.entityManager = em;
    }

    public List<Region> findAllChildren(String code) {
        JPAQuery<Region> query = new JPAQuery<>(entityManager);
        List<Region> result = new ArrayList<>();
        List<Region> tmp;
        query.select(qRegion).from(qRegion)
            .where(qRegion.parent.code.eq(code));
        tmp = query.fetch();
        while (CollectionUtils.isNotEmpty(tmp)) {
            result.addAll(tmp);
            tmp = findAllChildren(tmp);
        }
        return result;
    }

    private List<Region> findAllChildren(List<Region> parents) {
        JPAQuery<Region> query = new JPAQuery<>(entityManager);
        query.select(qRegion).from(qRegion).where(qRegion.parent.in(parents));
        return query.fetch();
    }
}
