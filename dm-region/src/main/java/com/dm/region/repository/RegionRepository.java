package com.dm.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.region.entity.Region;

public interface RegionRepository extends JpaRepository<Region, String>, QuerydslPredicateExecutor<Region> {

    List<Region> findAllByParentCode_CodeIsNull();

    List<Region> findAllByParentCode_Code(String code);

    List<Region> findAllChildren(String code);

}
