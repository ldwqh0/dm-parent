package com.dm.dingtalk.api.request;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * 待办事项请求模型
 * 
 * @author ldwqh0@outlook.com
 *
 */
public class OapiWorkrecordAddRequest implements Serializable {

    private static final long serialVersionUID = 5366602364489395358L;

    /**
     * 待办事项对应的用户id
     */
    private String userid;

    @JsonIgnore
    private ZonedDateTime createTime = ZonedDateTime.now();

    /**
     * 待办事项的标题，最多50个字符
     */
    private String title;

    /**
     * 待办事项的跳转链接
     */
    private String url;

    /**
     * pc端跳转url，不传则使用url参数
     */
    private String pcUrl;

    /**
     * 待办来源名称
     */
    private String sourceName;

    /**
     * 待办的pc打开方式。2表示在pc端打开，4表示在浏览器打开<br>
     * 默认为2
     */
    private Integer pcOpenType = 2;

    /**
     * 外部业务id，建议带上业务方来源字段，防止与其他业务方冲突<br>
     * 
     * 比如一个请假业务，在自由的系统中，某次请假的业务ID为 103 <br>
     * 
     * 则对于该业务的待办只能存在一条，不能对某个业务有多个重复的待办事项
     */
    private String bizId;

    /**
     * 待办事项表单<br >
     * 这个表单显示在待办事项的消息里面<br>
     * 每人每天最多收到一条表单内容相同的待办，这里的表单内容，包括title和formItemList参数
     */
    private List<FormItem> formItemList = new ArrayList<>();

    @JsonGetter("create_time")
    public Long getCreateTime() {
        return createTime.toInstant().toEpochMilli();
    }

    public String getUserid() {
        return userid;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPcUrl() {
        return pcUrl;
    }

    @JsonGetter("source_name")
    public String getSourceName() {
        return sourceName;
    }

    @JsonGetter(value = "pc_open_type")
    public Integer getPcOpenType() {
        return pcOpenType;
    }

    @JsonGetter("biz_id")
    public String getBizId() {
        return bizId;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPcUrl(String pcUrl) {
        this.pcUrl = pcUrl;
    }

    @JsonSetter("sourceName")
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setPcOpenType(Integer pcOpenType) {
        this.pcOpenType = pcOpenType;
    }

    @JsonSetter("bizId")
    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public List<FormItem> getFormItemList() {
        return formItemList;
    }

    public OapiWorkrecordAddRequest addFormItem(String title, String content) {
        this.formItemList.add(new FormItem(title, content));
        return this;
    }

    /**
     * 待办事项表单
     * 
     * @author ldwqh0@outlook.com
     *
     */
    static class FormItem {
        /**
         * 表单标题
         */
        private String title;

        /**
         * 表单内容
         */
        private String content;

        public FormItem(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public FormItem() {
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}
