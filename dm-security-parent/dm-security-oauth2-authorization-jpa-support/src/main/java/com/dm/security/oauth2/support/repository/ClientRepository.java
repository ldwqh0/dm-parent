package com.dm.security.oauth2.support.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.security.oauth2.support.entity.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

}
