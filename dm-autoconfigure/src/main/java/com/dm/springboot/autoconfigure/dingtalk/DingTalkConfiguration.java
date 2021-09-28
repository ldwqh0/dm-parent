package com.dm.springboot.autoconfigure.dingtalk;

import com.dm.dingtalk.api.callback.CallbackController;
import com.dm.dingtalk.api.callback.CallbackProperties;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.dingtalk.api.service.DingtalkAccessTokenService;
import com.dm.dingtalk.api.service.impl.DefaultDingTalkServiceImpl;
import com.dm.dingtalk.api.service.impl.OrganizationInternalAppAccessTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "dingtalk")
@ConditionalOnClass(DefaultDingTalkServiceImpl.class)
@ConditionalOnProperty(prefix = "dingtalk", name = {"appkey", "appsecret"})
public class DingTalkConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DingTalkConfiguration.class);

    private String appkey;
    private String appsecret;

    private String corpId;

    private String customKey;

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

    protected String getCustomKey() {
        return customKey;
    }

    protected void setCustomKey(String customKey) {
        this.customKey = customKey;
    }

    @Bean
    @ConditionalOnProperty(prefix = "dingtalk", name = {"corp-id"})
    public DingtalkAccessTokenService organizationInternalAppAccessTokenService() {
        log.info("Init default OrganizationInternalAppAccessTokenService with appkey[{}]", appkey);
        return new OrganizationInternalAppAccessTokenService(appkey, appsecret, corpId);
    }

    @Bean
    @ConditionalOnMissingBean(DingTalkService.class)
    @ConditionalOnBean(DingtalkAccessTokenService.class)
    public DingTalkService dingTalkService(@Autowired DingtalkAccessTokenService accessTokenService) {
        DefaultDingTalkServiceImpl defaultDingTalkService = new DefaultDingTalkServiceImpl();
        defaultDingTalkService.setDingtalkAccessTokenService(accessTokenService);
        return defaultDingTalkService;
    }

    @Bean
    @ConditionalOnMissingBean(DingtalkConfigurerAdapter.class)
    @ConditionalOnProperty(prefix = "dingtalk.callback", name = {"aes-key", "token"})
    public DingtalkConfigurerAdapter adapter() {
        return new DingtalkConfigurerAdapter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "dingtalk.callback", name = {"aes-key", "token"})
    public CallbackController callbackController(@Autowired ObjectMapper om) {
        CallbackController c = new CallbackController();
        DingtalkConfigurerAdapter adapter = adapter();
        c.setCallbackProperties(mergeProperties(callback));
        c.setObjectMapper(om);
        c.setHandlers(adapter.getConsumers());
        // 设置消息加密解密的key
        if (StringUtils.isNotEmpty(corpId)) {
            c.setEnvkey(corpId);
        } else if (StringUtils.isNotEmpty(customKey)) {
            c.setEnvkey(customKey);
        }
        return c;
    }

    private CallbackProperties mergeProperties(CallbackProperties properties) {
        return properties;
    }

}
