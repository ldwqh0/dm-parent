package com.dm.auth.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;

public interface ClientInfoService {

	/**
	 * 判断是否存在client
	 * 
	 * @return
	 */
	public boolean exist();

	/**
	 * 保存一个client
	 * 
	 * @param client
	 * @return
	 */
	public ClientInfo save(ClientInfoDto client);

	/**
	 * 删除一个client
	 * 
	 * @param clientId
	 */
	public void delete(String clientId);

	/**
	 * 根据id查找client
	 * 
	 * @param id
	 * @return
	 */
	public Optional<ClientInfo> findById(String id);

	/**
	 * 查找client
	 * 
	 * @param key
	 * @param pageable
	 * @return
	 */
	public Page<ClientInfo> find(String key, Pageable pageable);

	/**
	 * 更新client
	 * 
	 * @param id
	 * @param client
	 * @return
	 */
	public ClientInfo update(String id, ClientInfoDto client);

}
