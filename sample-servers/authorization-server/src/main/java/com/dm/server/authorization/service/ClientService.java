package com.dm.server.authorization.service;

import com.dm.server.authorization.dto.ClientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.xyyh.authorization.client.ClientDetailsService;

import java.util.Optional;

/**
 *
 * <p>服务器安全配置</p>
 *
 * @author ldwqh0@outlook.com
 */
public interface ClientService extends ClientDetailsService {

    ClientDto save(ClientDto client);

    boolean existAnyClient();

    void delete(String clientId);

    Optional<ClientDto> findById(String id);

    ClientDto update(String id, ClientDto client);

    Page<ClientDto> find(String key, Pageable pageable);
}
