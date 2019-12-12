package com.dm.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.region.entity.Region;

public interface RegionRepository extends JpaRepository<Region, String>, QuerydslPredicateExecutor<Region> {

    public List<Region> findAllByParentCode_CodeIsNull();

    public List<Region> findAllByParentCode_Code(String code);

    public List<Region> findAllChildren(String code);
}
