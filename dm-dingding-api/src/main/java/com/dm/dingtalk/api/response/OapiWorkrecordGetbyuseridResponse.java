package com.dm.dingtalk.api.response;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Data;

/**
 * 获取用户待办事项的响应
 * 
 * @author ldwqh0@outlook.com
 *
 */
public class OapiWorkrecordGetbyuseridResponse extends TaobaoResponse {

    private static final long serialVersionUID = 7104928664184621680L;

    private Long errcode;

    private String errmsg;

    private OapiWorkRecordPageResult records;

    public Long getErrcode() {
        return errcode;
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public OapiWorkRecordPageResult getRecords() {
        return records;
    }

    public void setRecords(OapiWorkRecordPageResult records) {
        this.records = records;
    }

}

class OapiWorkRecordPageResult implements Serializable {

    private static final long serialVersionUID = -6596878216424209615L;

    private boolean hasMore = false;

    private List<OapiWorkRecord> list;

    public boolean isHasMore() {
        return hasMore;
    }

    @JsonSetter("has_more")
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<OapiWorkRecord> getList() {
        return list;
    }

    public void setList(List<OapiWorkRecord> list) {
        this.list = list;
    }

}

class OapiWorkRecord implements Serializable {
    private static final long serialVersionUID = 5999418976457234703L;
    private String recordId;
    private ZonedDateTime createTime;
    private String title;
    private String url;
    private List<FormItem> forms;

    public String getRecordId() {
        return recordId;
    }

    @JsonSetter("record_id")
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    @JsonSetter("create_time")
    public void setCreateTime(Long createTime) {
        this.createTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(createTime), ZoneId.of("Asia/Shanghai"));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<FormItem> getForms() {
        return forms;
    }

    public void setForms(List<FormItem> forms) {
        this.forms = forms;
    }

    @Data
    static class FormItem {
        private String title;
        private String content;
    }
}
