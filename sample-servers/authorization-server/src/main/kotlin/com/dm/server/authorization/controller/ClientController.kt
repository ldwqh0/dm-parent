package com.dm.server.authorization.controller

import com.dm.common.dto.ValidationResult
import com.dm.common.exception.DataNotExistException
import com.dm.server.authorization.dto.ClientDto
import com.dm.server.authorization.service.ClientService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * 应用连接程序
 *
 * @author 李东
 * @Copyright (C) 2019 江苏云智网络科技股份有限公司版权所有
 */
@RestController
@RequestMapping("clients")
class ClientController(
    val clientService: ClientService
) {
    /**
     * 新增连接程序
     *
     * @param client 连接程序
     * @return 新建后的连接程序
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody client: ClientDto): ClientDto {
        return clientService.save(client)
    }

    /**
     * 删除一个连接程序
     *
     * @param appId 要删除的连接的appId
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") appId: String) {
        clientService.delete(appId)
    }

    /**
     * 修改一个应用连接程序
     *
     * @param appId  要修改的连接程序的appId
     * @param client 新的连接信息
     * @return 修改后的连接信息
     */
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun update(@PathVariable("id") appId: String, @RequestBody client: ClientDto): ClientDto {
        return clientService.update(appId, client)
    }

    /**
     * 获取应用列表
     *
     * @param keyword  查询关键字
     * @param pageable 分页参数
     * @return [Pageable]
     */
    @GetMapping
    fun listClients(@RequestParam("keyword") keyword: String?, pageable: Pageable): Page<ClientDto>? {
        return clientService.find(keyword, pageable)
    }

    /**
     * 根据ID获取应用列表
     *
     * @param appId 应用ID
     * @return 查找到的应用
     */
    @GetMapping("{id}")
    fun findById(@PathVariable("id") appId: String): ClientDto {
        return clientService.findById(appId) ?: throw DataNotExistException()
    }

    @GetMapping(value = ["validation"], params = ["name"])
    fun validateName(
        @RequestParam("name") name: String,
        @RequestParam(value = "exclude", required = false) exclude: String? = null
    ): ValidationResult {
        return if (clientService.existsByName(name, exclude)) {
            ValidationResult.failure()
        } else {
            ValidationResult.success()
        }
    }
}
