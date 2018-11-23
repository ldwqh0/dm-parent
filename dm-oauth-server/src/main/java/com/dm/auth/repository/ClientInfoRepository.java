package com.dm.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.auth.entity.ClientInfo;

public interface ClientInfoRepository extends JpaRepository<ClientInfo, String> {
	Page<ClientInfo> findByNameContains(String name, Pageable pageable);

	Page<ClientInfo> find(String key, Pageable pageable);
}
