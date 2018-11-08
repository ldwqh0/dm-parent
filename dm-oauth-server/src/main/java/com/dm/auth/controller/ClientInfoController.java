package com.dm.auth.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.converter.ClientInfoConverter;
import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;
import com.dm.auth.service.ClientInfoService;
import com.dm.common.dto.TableResultDto;

import static org.springframework.http.HttpStatus.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("clients")
public class ClientInfoController {

	@Autowired
	private ClientInfoService clientService;

	@Autowired
	private ClientInfoConverter clientInfoConverter;

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

	@GetMapping(params = { "draw" })
	public TableResultDto<ClientInfoDto> search(
			@RequestParam(value = "draw", defaultValue = "1") Long draw,
			@RequestParam(value = "key", required = false) String key,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ClientInfo> clients = clientService.find(key, pageable);
		return TableResultDto.success(draw, clients, client -> clientInfoConverter.toDto(client));
	}
}
