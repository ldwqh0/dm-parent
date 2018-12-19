package com.leadingsoft.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.leadingsoft.dto.Column;
import com.leadingsoft.dto.Table;
import com.leadingsoft.service.DatabaseMetaService;

/**
 * @author LiDong
 *
 */
@Service
public class DatabaseMetaServiceImpl implements DatabaseMetaService {

	@Override
	public List<Table> listTables(Connection cnn) throws SQLException {
		String catalog = cnn.getCatalog();
		String schema = cnn.getSchema();
		DatabaseMetaData databaseMetaData = cnn.getMetaData();
		ResultSet tablesResult = databaseMetaData.getTables(catalog, schema, null, new String[] { "TABLE" });
		var tables = new ArrayList<Table>();
		while (tablesResult.next()) {
			Table table = new Table();
			table.setCatalog(tablesResult.getString("TABLE_CAT"));
			table.setTableName(tablesResult.getString("TABLE_NAME"));
			table.setSchema(tablesResult.getString("TABLE_SCHEM"));
			setColumns(databaseMetaData, table);
			tables.add(table);
		}
		return tables;
	}

	private void setColumns(DatabaseMetaData metaData, Table table) throws SQLException {
		String catalog = table.getCatalog();
		String schema = table.getSchema();
		String tableName = table.getTableName();
		var columns = new HashSet<Column>();
		var columnResult = metaData.getColumns(catalog, schema, tableName, null);
		while (columnResult.next()) {
			Column column = new Column();
			column.setAutoincrement(getBoolean(columnResult, "IS_AUTOINCREMENT"));
			column.setColumnName(columnResult.getString("COLUMN_NAME"));
			column.setNullable(columnResult.getBoolean("NULLABLE"));
			column.setLength(columnResult.getInt("COLUMN_SIZE"));
			column.setType(columnResult.getString("TYPE_NAME"));
			column.setDataType(columnResult.getInt("DATA_TYPE"));
			column.setComment(columnResult.getString("REMARKS"));
			columns.add(column);
			System.out.println(columnResult.getString("REMARKS"));
			var columnMetaData = columnResult.getMetaData();
			var columnCount = columnMetaData.getColumnCount();
			System.out.println("------------------------");
			for (int i = 1; i <= columnCount; i++) {
				System.out.println(columnMetaData.getColumnLabel(i));
			}
			System.out.println("------------------------");

		}
		table.setColumns(columns);
	}

	private boolean getBoolean(ResultSet res, String name) throws SQLException {
		String str = res.getString(name);
		if ("1".equals(str) || "YES".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
}
