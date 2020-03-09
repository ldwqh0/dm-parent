package com.dm.dingtalk.api.callback.model;

import com.fasterxml.jackson.annotation.JsonGetter;

public class CallbackResponse {

    private String signature;
    private String timeStamp;
    private String nonce;
    private String encrypt;

    public CallbackResponse(String signature, String time, String nonce, String encrypt) {
        this.signature = signature;
        this.nonce = nonce;
        this.timeStamp = time;
        this.encrypt = encrypt;
    }

    @JsonGetter("msg_signature")
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

}
