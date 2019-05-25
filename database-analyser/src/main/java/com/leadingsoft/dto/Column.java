package com.leadingsoft.dto;

import lombok.Data;

@Data
public class Column {
	private String columnName;
	private boolean autoincrement;
	private boolean nullable;
	private int length;
	private String type;
	private int dataType;
	private String comment;
}
