package com.dm.springboot.autoconfigure.dingtalk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dm.dingtalk.api.callback.CallbackController;
import com.dm.dingtalk.api.callback.CallbackProperties;
import com.dm.dingtalk.api.model.DingClientConfig;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.dingtalk.api.service.impl.DefaultDingTalkServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnClass(DefaultDingTalkServiceImpl.class)
@Configuration
@Slf4j
public class DingTalkConfiguration {

    @Bean
    @ConditionalOnMissingBean(DingTalkService.class)
    @ConditionalOnBean(DingClientConfig.class)
    public DingTalkService dingTalkService(@Autowired DingClientConfig config) {
        log.info("Init default dingclient with appkey[{}]", config.getAppkey());
        return new DefaultDingTalkServiceImpl(config);
    }

    @ConfigurationProperties(prefix = "dingtalk")
    @Configuration
    @ConditionalOnProperty(prefix = "dingtalk", name = { "appkey", "appsecret" })
    @EnableConfigurationProperties(DingClientConfigCongiguration.class)
    public static class DingClientConfigCongiguration {
        private String appkey;
        private String appsecret;
        private String corpId;

        private CallbackProperties callback;

        public String getAppkey() {
            return appkey;
        }

        public void setAppkey(String appkey) {
            this.appkey = appkey;
        }

        public String getAppsecret() {
            return appsecret;
        }

        public void setAppsecret(String appsecret) {
            this.appsecret = appsecret;
        }

        public CallbackProperties getCallback() {
            return callback;
        }

        public void setCallback(CallbackProperties callback) {
            this.callback = callback;
        }

        public String getCorpId() {
            return corpId;
        }

        public void setCorpId(String corpId) {
            this.corpId = corpId;
        }

        @Bean
        public DingClientConfig clientConfig() {
            return new DingClientConfig(appkey, appsecret, corpId);
        }

        @Bean
        @ConditionalOnMissingBean(DingtalkConfigurerAdpater.class)
        @ConditionalOnProperty(prefix = "dingtalk.callback", name = { "aes-key", "token" })
        public DingtalkConfigurerAdpater adpater() {
            return new DingtalkConfigurerAdpater();
        }

        @Bean
        @ConditionalOnProperty(prefix = "dingtalk.callback", name = { "aes-key", "token" })
        public CallbackController callbackController(@Autowired ObjectMapper om) {
            CallbackController c = new CallbackController();
            DingtalkConfigurerAdpater adpater = adpater();
            c.setCallbackProperties(mergeProperties(callback, adpater));
            c.setObjectMapper(om);
            c.setHandlers(adpater.getConsumers());
            c.setDingClientConfig(clientConfig());
            return c;
        }

        private CallbackProperties mergeProperties(CallbackProperties properties, DingtalkConfigurerAdpater adpater) {
            return properties;
        }
    }

}
