package com.dm.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.auth.entity.ClientInfo;

public interface ClientInfoRepository extends JpaRepository<ClientInfo, String> {

}
