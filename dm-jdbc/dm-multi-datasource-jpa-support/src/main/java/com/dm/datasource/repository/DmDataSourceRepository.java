package com.dm.datasource.repository;

import com.dm.datasource.entity.DmDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DmDataSourceRepository extends JpaRepository<DmDataSource, Long>, QuerydslPredicateExecutor<DmDataSource> {
}
