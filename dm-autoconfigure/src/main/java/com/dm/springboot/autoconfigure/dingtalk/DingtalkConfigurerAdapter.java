//package com.dm.springboot.autoconfigure.dingtalk;
//
//import com.dm.dingtalk.api.callback.Event;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Consumer;
//
//public class DingtalkConfigurerAdapter {
//
//    private List<String> callbackTag;
//
//    private String token;
//
//    private String aeskey;
//
//    private String url;
//
//    private final Map<String, Consumer<Event>> consumers = new HashMap<>();
//
//    public Map<String, Consumer<Event>> getConsumers() {
//        return consumers;
//    }
//
//    public DingtalkConfigurerAdapter setCallbackTag(List<String> callBackTag) {
//        this.callbackTag = callBackTag;
//        return this;
//    }
//
//    public DingtalkConfigurerAdapter setToken(String token) {
//        this.token = token;
//        return this;
//    }
//
//    public DingtalkConfigurerAdapter setAeskey(String aeskey) {
//        this.aeskey = aeskey;
//        return this;
//    }
//
//    public DingtalkConfigurerAdapter setUrl(String url) {
//        this.url = url;
//        return this;
//    }
//
//    public List<String> getCallbackTag() {
//        return callbackTag;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public String getAeskey() {
//        return aeskey;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public final void registryConsumer(String eventType, Consumer<Event> consumer) {
//        this.consumers.put(eventType, consumer);
//    }
//}
