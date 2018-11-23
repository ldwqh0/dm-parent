package com.dm.fileserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("file")
public class FileController {

	@GetMapping
	public String get() {
		return "asdgljasldg";
	}

}
