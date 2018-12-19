package com.leadingsoft.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.leadingsoft.dto.Table;

public interface DatabaseMetaService {

	List<Table> listTables(Connection cnn) throws SQLException;

}
