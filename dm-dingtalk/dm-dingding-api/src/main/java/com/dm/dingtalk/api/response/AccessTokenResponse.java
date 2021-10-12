package com.dm.dingtalk.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Objects;

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

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public ZonedDateTime getExpireDate() {
        return expireDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessTokenResponse)) return false;
        AccessTokenResponse that = (AccessTokenResponse) o;
        return Objects.equals(expiresIn, that.expiresIn) && Objects.equals(accessToken, that.accessToken) && Objects.equals(cropId, that.cropId) && Objects.equals(expireDate, that.expireDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiresIn, accessToken, cropId, expireDate);
    }
}
