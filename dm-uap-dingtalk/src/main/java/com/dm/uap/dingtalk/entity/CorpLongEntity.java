package com.dm.uap.dingtalk.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;

import lombok.Getter;

@MappedSuperclass
@IdClass(CorpLongId.class)
@Getter
public abstract class CorpLongEntity implements Serializable {

    private static final long serialVersionUID = 1856254435464039720L;

    @Id
    @Column(name = "corp_id_")
    private String corpId;

    /**
     * id
     */
    @Id
    @Column(name = "id_")
    private Long id;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((corpId == null) ? 0 : corpId.hashCode());
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
        CorpLongEntity other = (CorpLongEntity) obj;
        if (corpId == null) {
            if (other.corpId != null)
                return false;
        } else if (!corpId.equals(other.corpId))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    protected void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    protected CorpLongEntity(String corpId, Long id) {
        super();
        this.corpId = corpId;
        this.id = id;
    }

    CorpLongEntity() {
        super();
    }

}
