package com.dm.code;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "dm_code_")
public class Code {

	@ManyToOne
	@JoinColumn(name = "code_type_")
	private CodeType type;

	@Column(name = "code_")
	private String code;
}
