package com.dm.zuul.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.zuul.security.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
