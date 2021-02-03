package com.dm.dingtalk.api.callback;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class CallbackProperties {

    private Set<String> callbackTags;

    private String token;

    private String aesKey;

    private String url;

    @JsonSetter("call-back-tags")
    public void setCallbackTags(Set<String> callbackTags) {
        this.callbackTags = callbackTags;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonSetter("aes-key")
    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonGetter("call_back_tag")
    public Set<String> getCallbackTags() {
        return callbackTags;
    }

    @JsonGetter("token")
    public String getToken() {
        return token;
    }

    @JsonGetter("aes_key")
    public String getAesKey() {
        return aesKey;
    }

    @JsonGetter("url")
    public String getUrl() {
        return url;
    }

}
