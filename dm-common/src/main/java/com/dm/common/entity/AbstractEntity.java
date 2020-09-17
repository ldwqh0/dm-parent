package com.dm.common.entity;

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
public abstract class AbstractEntity implements Persistable<Long> {

    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", pkColumnName = "table_name_", table = "auto_pk_support_", valueColumnName = "next_id_")
    @Id
    @Column(name = "id_", updatable = false)
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
        // 子类可能复写id,所以通过判断 getId()能否获取到数据决定是否新对象，而不是通过本类中的id属性判断是否新对象
        return Objects.isNull(getId());
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
            return other.id == null;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
