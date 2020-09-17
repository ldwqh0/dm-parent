package com.dm.springboot.autoconfigure.dingtalk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.dm.dingtalk.api.callback.Event;

public class DingtalkConfigurerAdpater {

    private List<String> callbackTag;

    private String token;

    private String aeskey;

    private String url;

    private final Map<String, Consumer<Event>> consumers = new HashMap<>();

    public Map<String, Consumer<Event>> getConsumers() {
        return consumers;
    }

    public DingtalkConfigurerAdpater setCallbackTag(List<String> callBackTag) {
        this.callbackTag = callBackTag;
        return this;
    }

    public DingtalkConfigurerAdpater setToken(String token) {
        this.token = token;
        return this;
    }

    public DingtalkConfigurerAdpater setAeskey(String aeskey) {
        this.aeskey = aeskey;
        return this;
    }

    public DingtalkConfigurerAdpater setUrl(String url) {
        this.url = url;
        return this;
    }

    public List<String> getCallbackTag() {
        return callbackTag;
    }

    public String getToken() {
        return token;
    }

    public String getAeskey() {
        return aeskey;
    }

    public String getUrl() {
        return url;
    }

    public final void registryConsumer(String eventType, Consumer<Event> consumer) {
        this.consumers.put(eventType, consumer);
    }
}
