package com.dm.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.region.entity.Region;

public interface RegionRepository extends JpaRepository<Region, String> {

	List<Region> findAllByParentCode_CodeIsNull();

	List<Region> findAllByParentCode_Code(String code);
}
