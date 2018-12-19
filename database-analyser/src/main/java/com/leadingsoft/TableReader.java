package com.leadingsoft;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leadingsoft.dto.Table;
import com.leadingsoft.service.DatabaseMetaService;

@RestController
@RequestMapping("test")
@SpringBootApplication
public class TableReader {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private DatabaseMetaService metaService;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TableReader.class, args);
	}

	@GetMapping
	public List<Table> doIt() throws SQLException {
		try (Connection cnn = dataSource.getConnection()) {
			return metaService.listTables(cnn);
		}
	}
}
