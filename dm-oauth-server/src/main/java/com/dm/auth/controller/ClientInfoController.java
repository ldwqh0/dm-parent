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

import com.dm.auth.converter.ClientInfoConverter;
import com.dm.auth.dto.AccessTokenInfoDto;
import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;
import com.dm.auth.service.AccessTokenService;
import com.dm.auth.service.ClientInfoService;
import com.dm.common.dto.TableResult;

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
	private ClientInfoService clientService;

	@Autowired
	private ClientInfoConverter clientInfoConverter;

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
	public ClientInfoDto get(@PathVariable("id") String id) {
		Optional<ClientInfo> info = clientService.findById(id);
		return clientInfoConverter.toDto(info);
	}

	@PostMapping
	@ResponseStatus(CREATED)
	public ClientInfoDto save(@RequestBody ClientInfoDto client) {
		ClientInfo client_ = clientService.save(client);
		return clientInfoConverter.toDto(client_);
	}

	@PutMapping("{id}")
	@ResponseStatus(CREATED)
	public ClientInfoDto update(@PathVariable("id") String id, @RequestBody ClientInfoDto client) {
		ClientInfo client_ = clientService.update(id, client);
		return clientInfoConverter.toDto(client_);
	}

	@GetMapping(params = { "draw" })
	public TableResult<ClientInfoDto> search(
			@RequestParam(value = "draw", defaultValue = "1") Long draw,
			@RequestParam(value = "search", required = false) String key,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ClientInfo> clients = clientService.find(key, pageable);
		return TableResult.success(draw, clients, clientInfoConverter::toDto);
	}

	@GetMapping(value = "{client}/tokens", params = { "draw" })
	public TableResult<AccessTokenInfoDto> tokens(
			@RequestParam("draw") Long draw,
			@RequestParam(value = "search", required = false) String key,
			@PageableDefault Pageable pageable,
			@PathVariable("client") String client) {
		Page<AccessTokenInfoDto> tokens = tokenService.listTokensByClient(client, pageable);
		return TableResult.success(draw, tokens, token -> token);
	}

	@DeleteMapping("/tokens/{token}")
	@ResponseStatus(NO_CONTENT)
	public void revokeToken(@PathVariable("token") String token) {
		consumerTokenServices.revokeToken(token);
	}
}
