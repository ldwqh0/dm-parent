package com.dm.region.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import com.dm.collections.CollectionUtils;
import com.dm.region.entity.QRegion;
import com.dm.region.entity.Region;
import com.querydsl.jpa.impl.JPAQuery;

@RequiredArgsConstructor
public class RegionRepositoryImpl {

    private final EntityManager em;


    private final QRegion qRegion = QRegion.region;

    public List<Region> findAllChildren(String code) {
        JPAQuery<Region> query = new JPAQuery<>(em);
        List<Region> result = new ArrayList<>();
        List<Region> tmp;
        query.select(qRegion).from(qRegion)
            .where(qRegion.parentCode.code.eq(code));
        tmp = query.fetch();
        while (CollectionUtils.isNotEmpty(tmp)) {
            result.addAll(tmp);
            tmp = findAllChildren(tmp);
        }
        return result;
    }

    private List<Region> findAllChildren(List<Region> parents) {
        JPAQuery<Region> query = new JPAQuery<>(em);
        query.select(qRegion).from(qRegion).where(qRegion.parentCode.in(parents));
        return query.fetch();
    }
}
