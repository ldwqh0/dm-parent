package com.dm.server.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import  com.dm.server.authorization.entity.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

}
