package com.dm.dingtalk.api.request;

import java.io.Serializable;

public class OapiWorkrecordGetbyuseridRequest implements Serializable {

    private static final long serialVersionUID = 7028465206865165624L;

    private String userid;

    private int offset = 0;

    private int limit = 50;

    private byte status = 0;

    public static OapiWorkrecordGetbyuseridRequest of(String userid) {
        return new OapiWorkrecordGetbyuseridRequest(userid);
    }

    public static OapiWorkrecordGetbyuseridRequest of(String userid, int offset, int limit, byte status) {
        return new OapiWorkrecordGetbyuseridRequest(userid, offset, limit, status);
    }

    public OapiWorkrecordGetbyuseridRequest(String userid, int offset, int limit, byte status) {
        this.userid = userid;
        this.offset = offset;
        this.limit = limit;
        this.status = status;
    }

    public OapiWorkrecordGetbyuseridRequest(String userid) {
        this.userid = userid;
    }

    public OapiWorkrecordGetbyuseridRequest() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

}
