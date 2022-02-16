package com.dm.dingtalk.api.response;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 获取用户待办事项的响应
 *
 * @author ldwqh0@outlook.com
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

    @Override
    public String getErrmsg() {
        return errmsg;
    }

    @Override
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

    static class FormItem implements Serializable {
        private static final long serialVersionUID = -1997517300506297746L;
        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FormItem formItem = (FormItem) o;
            return Objects.equals(title, formItem.title) && Objects.equals(content, formItem.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, content);
        }
    }
}
