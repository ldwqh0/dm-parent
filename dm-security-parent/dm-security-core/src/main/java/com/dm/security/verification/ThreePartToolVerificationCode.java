package com.dm.security.verification;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 第三方工具验证码实体
 * 
 * @author LiDong
 *
 */
public class ThreePartToolVerificationCode implements Serializable {

    private static final long serialVersionUID = -6060058106901443139L;

    private String id;

    private String key;

    private String code;

    private ZonedDateTime createdDate = ZonedDateTime.now();

    private ZonedDateTime expireAt;

    public ThreePartToolVerificationCode() {
        super();
    }

    public ThreePartToolVerificationCode(String id, String key, String code, ZonedDateTime expireAt) {
        super();
        this.id = id;
        this.key = key;
        this.code = code;
        this.expireAt = expireAt;
    }

    /**
     * 验证码ID
     * 
     * @return
     */
    public String getId() {
        return this.id;
    };

    /**
     * 要验证的第三方工具，通常是手机号，邮箱地址等
     * 
     * @return
     */
    @JsonIgnore
    public String getKey() {
        return this.key;
    };

    /**
     * 生成的验证码
     * 
     * @return
     */
    @JsonIgnore
    public String getCode() {
        return this.code;
    };

    /**
     * 验证码生成时间
     * 
     * @return
     */
    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    };

    /**
     * 失效时间
     * 
     * @return
     */
    public ZonedDateTime getExpireAt() {
        return this.expireAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setExpireAt(ZonedDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result + ((expireAt == null) ? 0 : expireAt.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
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
        ThreePartToolVerificationCode other = (ThreePartToolVerificationCode) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (createdDate == null) {
            if (other.createdDate != null)
                return false;
        } else if (!createdDate.equals(other.createdDate))
            return false;
        if (expireAt == null) {
            if (other.expireAt != null)
                return false;
        } else if (!expireAt.equals(other.expireAt))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
