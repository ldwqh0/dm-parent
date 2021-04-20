package com.dm.dingtalk.api.response;

import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessTokenResponse extends TaobaoResponse {

    private static final long serialVersionUID = -866304342538258608L;

    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    @JsonProperty("access_token")
    private String accessToken;

    /**
     * token所属企业/组织
     */
    private String cropId;

    /**
     * token过期时间
     */
    private ZonedDateTime expireDate;

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        if (Objects.nonNull(expiresIn)) {
            // 这里修正一下token过期时间
            setExpireDate(ZonedDateTime.now().plusSeconds(expiresIn - 60));
        }
    }

    private void setExpireDate(ZonedDateTime expireDate) {
        this.expireDate = expireDate;
    }
}
