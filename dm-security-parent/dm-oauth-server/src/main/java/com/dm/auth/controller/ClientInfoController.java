package com.dm.auth.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.dto.AccessTokenInfoDto;
import com.dm.auth.service.AccessTokenService;
import com.dm.common.exception.DataValidateException;
import com.dm.security.oauth2.support.converter.ClientConverter;
import com.dm.security.oauth2.support.dto.ClientDto;
import com.dm.security.oauth2.support.entity.Client;
import com.dm.security.oauth2.support.service.ClientService;

import static org.springframework.http.HttpStatus.*;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;

@RestController
@RequestMapping("clients")
public class ClientInfoController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientConverter clientInfoConverter;

    @Autowired
    private AccessTokenService tokenService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @DeleteMapping("{clientId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("clientId") String clientId) {
        clientService.delete(clientId);
    }

    @GetMapping("{id}")
    public ClientDto get(@PathVariable("id") String id) {
        Optional<Client> info = clientService.findById(id);
        return clientInfoConverter.toDto(info.orElseThrow(DataValidateException::new));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ClientDto save(@RequestBody ClientDto client) {
        Client client_ = clientService.save(client);
        return clientInfoConverter.toDto(client_);
    }

    @PutMapping("{id}")
    @ResponseStatus(CREATED)
    public ClientDto update(@PathVariable("id") String id, @RequestBody ClientDto client) {
        Client client_ = clientService.update(id, client);
        return clientInfoConverter.toDto(client_);
    }

    @GetMapping(params = { "draw" })
    public Page<ClientDto> search(
            @RequestParam(value = "search", required = false) String key,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Client> clients = clientService.find(key, pageable);
        return clients.map(clientInfoConverter::toDto);
    }

    @GetMapping(value = "{client}/tokens", params = { "draw" })
    public Page<AccessTokenInfoDto> tokens(
            @RequestParam(value = "search", required = false) String key,
            @PageableDefault Pageable pageable,
            @PathVariable("client") String client) {
        return tokenService.listTokensByClient(client, pageable);
    }

    @DeleteMapping("/tokens/{token}")
    @ResponseStatus(NO_CONTENT)
    public void revokeToken(@PathVariable("token") String token) {
        consumerTokenServices.revokeToken(token);
    }
}
