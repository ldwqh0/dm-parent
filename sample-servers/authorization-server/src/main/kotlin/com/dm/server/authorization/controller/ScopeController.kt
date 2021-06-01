package com.dm.server.authorization.controller

import com.dm.server.authorization.service.ClientService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("scopes")
class ScopeController(
    val clientService: ClientService
) {
    @GetMapping
    fun findScopes(
        @RequestParam(value = "keyword", required = false, defaultValue = "") keyword: String,
        pageable: Pageable
    ): Page<String> {
        return clientService.findScopes(keyword, pageable)
    }
}
