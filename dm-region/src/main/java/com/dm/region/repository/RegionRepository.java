package com.dm.region.repository;

import com.dm.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, String>, QuerydslPredicateExecutor<Region> {

    List<Region> findAllByParentCode_CodeIsNull();

    List<Region> findAllByParentCode_Code(String code);

    List<Region> findAllChildren(String code);

    Optional<Region> findByHrefNotNullAndSyncedIsFalse();
}
