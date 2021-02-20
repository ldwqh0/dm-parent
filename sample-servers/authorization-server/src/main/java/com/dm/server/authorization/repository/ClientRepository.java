package com.dm.server.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.server.authorization.entity.Client;

/**
 * <p>ClientRepository</p>
 *
 * @author ldwqh0@outlook.com
 */
public interface ClientRepository extends JpaRepository<Client, String> {

}
