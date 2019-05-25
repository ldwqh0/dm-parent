package com.leadingsoft.dto;

import java.util.Set;

import lombok.Data;

@Data
public class Table {
	private String tableName;
	private String catalog;
	private String schema;
	private String comment;
	private Set<Column> columns;

	@Override
	public String toString() {
		return "Table [name=" + tableName + ", catalog=" + catalog + ", schema=" + schema + "]";
	}

}
