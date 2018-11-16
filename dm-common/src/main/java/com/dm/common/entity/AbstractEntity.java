package com.dm.common.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class AbstractEntity implements Persistable<Long>, Serializable {

	private static final long serialVersionUID = 6496721356813862072L;

	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
	@TableGenerator(name = "tableGenerator", pkColumnName = "table_name_", table = "auto_pk_support_", valueColumnName = "next_id_", allocationSize = 50)
	@Id
	@Column(name = "id_")
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	@Override
	@Transient
	public boolean isNew() {
		return Objects.isNull(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
