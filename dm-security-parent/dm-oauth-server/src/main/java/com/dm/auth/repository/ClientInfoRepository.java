package com.dm.auth.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dm.auth.entity.ClientInfo;

public interface ClientInfoRepository extends JpaRepository<ClientInfo, String> {
    public Page<ClientInfo> findByNameContains(String name, Pageable pageable);

    public Page<ClientInfo> find(String key, Pageable pageable);

    public Optional<ClientInfo> findByName(String name);

//	@Query(nativeQuery = true, value = "select distinct scope from dm_client_info__scope")
    public Set<String> listScopes();
}
