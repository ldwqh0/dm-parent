package com.dm.auth.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.service.ClientInfoService;

import static org.springframework.http.HttpStatus.*;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("clients")
public class ClientInfoController {

	@Autowired
	private ClientInfoService clientService;

	@DeleteMapping("{clientId}")
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable("clientId") String clientId) {
		clientService.delete(clientId);
	}

	@GetMapping
	public String test() {
		return "success";
	}

}
