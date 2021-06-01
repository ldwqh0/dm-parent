package com.dm.server.authorization.service.impl

import com.dm.common.exception.DataNotExistException
import com.dm.common.exception.DataValidateException
import com.dm.server.authorization.converter.ClientConverter
import com.dm.server.authorization.dto.ClientDto
import com.dm.server.authorization.entity.Client
import com.dm.server.authorization.entity.QClient
import com.dm.server.authorization.repository.ClientRepository
import com.dm.server.authorization.service.ClientService
import com.querydsl.core.BooleanBuilder
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.xyyh.oidc.client.ClientDetails
import org.xyyh.oidc.client.ClientDetailsService
import org.xyyh.oidc.exception.NoSuchClientException

@Service
class ClientServiceImpl(
    private val clientRepository: ClientRepository
) : ClientService, ClientDetailsService {

    private val qClient: QClient = QClient.client

    @Transactional
    @CacheEvict(cacheNames = ["clients"], key = "#result.id")
    override fun save(client: ClientDto): ClientDto {
        if (existsByName(client.name)) {
            throw DataValidateException("指定的client已经存在");
        }
        val model = ClientConverter.copyProperties(Client(), client)
        return ClientConverter.toDto(clientRepository.save(model))
    }

    override fun existAnyClient(): Boolean {
        return clientRepository.count() > 0
    }

    @CacheEvict(cacheNames = ["clients"])
    override fun delete(clientId: String) {
        clientRepository.deleteById(clientId)
    }

    override fun findById(id: String): ClientDto? {
        return clientRepository.findByIdOrNull(id)?.let { ClientConverter.toDto(it) }
    }

    @Transactional
    @CacheEvict(cacheNames = ["clients"], key = "#id")
    override fun update(id: String, client: ClientDto): ClientDto {
        if (existsByName(client.name, id)) {
            throw DataValidateException("指定的client已经存在")
        }
        return clientRepository.findByIdOrNull(id)?.let {
            val result = ClientConverter.copyProperties(it, client)
            clientRepository.save(result)
            return ClientConverter.toDto(result)
        } ?: throw DataNotExistException()
    }

    @Transactional(readOnly = true)
    override fun find(key: String?, pageable: Pageable): Page<ClientDto> {
        val query = BooleanBuilder()
        if (!key.isNullOrEmpty()) {
            query.and(
                qClient.name.containsIgnoreCase(key)
                    .or(qClient.id.containsIgnoreCase(key))
            )
        }
        return clientRepository.findAll(query, pageable).map { ClientConverter.toListDto(it) }
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ["clients"], sync = true)
    override fun loadClientByClientId(clientId: String): ClientDetails {
        return this.clientRepository.findByIdOrNull(clientId)?.let {
            ClientConverter.toClientDetails(it)
        } ?: throw NoSuchClientException("the client $clientId is not exists")
    }

    override fun existsByName(name: String, exclude: String?): Boolean {
        return exclude?.let {
            clientRepository.existsByNameAndIdIsNot(name, it)
        } ?: clientRepository.existsByName(name)
    }

    override fun findScopes(keyword: String, pageable: Pageable): Page<String> {
        return clientRepository.findScopes(keyword, pageable);
    }
}
