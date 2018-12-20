package com.dm.uap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.uap.entity.Region;

public interface RegionRepository extends JpaRepository<Region, String> {

	List<Region> findAllByParentCode(String code);

}
