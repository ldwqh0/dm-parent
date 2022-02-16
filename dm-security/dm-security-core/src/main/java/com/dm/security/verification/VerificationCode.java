package com.dm.security.verification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class VerificationCode implements Serializable {

    private static final long serialVersionUID = -6060058106901443139L;

    /**
     * 验证码默认的失效时常，单位是分钟
     */
    private static final int DEFAULT_INVALIDATE_TIME = 5;

    private final String id;

    @JsonProperty(access = Access.WRITE_ONLY)
    private final String code;

    private final ZonedDateTime invalidateTime;

    private final String imgData;

    public VerificationCode(String id, String code, ZonedDateTime invalidateTime, String imgData) {
        this.id = id;
        this.code = code;
        this.invalidateTime = invalidateTime;
        this.imgData = imgData;
    }

    public VerificationCode(String id,
                            String code,
                            ZonedDateTime invalidateTime) {
        this(id, code, invalidateTime, null);
    }

    public VerificationCode(String id, String code) {
        this(id, code, null);
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public ZonedDateTime getInvalidateTime() {
        return invalidateTime;
    }

    public String getImgData() {
        return imgData;
    }

    public VerificationCode withImageData(String imgData) {
        return new VerificationCode(
            this.id,
            this.code,
            this.invalidateTime,
            imgData
        );
    }

}
