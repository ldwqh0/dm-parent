package com.dm.dingtalk.api.callback;

import com.dm.collections.Maps;
import com.dm.dingtalk.api.crypto.DingAes;
import com.dm.dingtalk.api.service.DingTalkService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

@RestController
@RequestMapping("dingTalk/callback")
public class CallbackController {

    private static final Logger log = LoggerFactory.getLogger(CallbackController.class);

    private Map<String, Consumer<Event>> handlers;

    private CallbackProperties callbackProperties;

    private ObjectMapper om;

    private DingAes aes;

    /**
     * 对于ISV开发来说，$key填写对应的suiteKey。<br>
     * <p>
     * 对于定制服务商开发来说，$key填写对应的customKey。<br>
     * <p>
     * 对于企业内部开发来说，$key填写企业的Corpid。<br>
     * <p>
     * 参考 <a href="https://ding-doc.dingtalk.com/doc#/faquestions/ltr370">钉钉开发文档</a>
     */
    private String envkey;

    private static final Integer RANDOM_LENGTH = 6;

    private static final String RANDOM_BASE_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Autowired
    private DingTalkService dingService;

    public void setCallbackProperties(CallbackProperties callbackProperties) {
        this.callbackProperties = callbackProperties;
        this.aes = new DingAes(callbackProperties.getAesKey());
    }

    public void setHandlers(Map<String, Consumer<Event>> handlers) {
        this.handlers = handlers;
    }

    public void setObjectMapper(ObjectMapper om) {
        this.om = om;
    }

    public void setEnvkey(String envkey) {
        this.envkey = envkey;
    }

    @PostMapping(params = {"signature"})
    public ResponseEntity<?> apply(
        @RequestParam("signature") String signature,
        @RequestParam("timestamp") Long timestamp,
        @RequestParam("nonce") String nonce,
        @RequestBody CallbackRequest rsp) {
        log.debug("接收到回调请求，signature={},timestamp={},nonce={}", signature, timestamp, nonce);
        String encrypt = rsp.getEncrypt();
        try {
            Event event = getEvent(encrypt, signature, timestamp, nonce);
            String eventType = event.getType();
            log.info("接收到回调请求，请求类型是 {}", eventType);
            if ("check_url".equals(eventType)) {
                return ResponseEntity.ok(okResponse());
            } else if (Maps.isNotEmpty(handlers)) {
                Consumer<Event> consumer = handlers.get(eventType);
                if (Objects.nonNull(consumer)) {
                    consumer.accept(event);
                }
            }
            return ResponseEntity.ok(okResponse());
        } catch (Exception e) {
            log.error("处理回调请求时发成错误", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    /**
     * 注册钉钉回调地址
     *
     * @return
     */
    @PostMapping("{corpid}")
    public String registry(@PathVariable("corpid") String corpid) {
        return dingService.registryCallback(corpid, callbackProperties);
    }

    /**
     * 取消回调注册
     *
     * @return
     */
    @DeleteMapping("{corpid}")
    public String delete(@PathVariable("corpid") String corpid) {
        return dingService.deleteCallback(corpid);
    }

    /**
     * 获取指定应用的回调错误信息
     *
     * @param corpid
     * @return
     */
    @GetMapping("errors")
    public String getCallbackError(@RequestParam("corpid") String corpid) {
        return dingService.getFailureCallback(corpid);
    }

    @GetMapping("errors/{corpid}")
    public String getCallbackError2(@PathVariable("corpid") String corpid) {
        return dingService.getFailureCallback(corpid);
    }

    /**
     * 构建一个表示处理成功的响应
     *
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private CallbackResponse okResponse()
        throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String time = String.valueOf(ZonedDateTime.now().toEpochSecond());
        String nonce = getRandomStr(RANDOM_LENGTH);
        EncryptMessage msg = EncryptMessage.of(nonce, "success", envkey);
        String encrypt = aes.encryptToBase64String(msg.toBytes());
        String signature = signature(callbackProperties.getToken(), time, nonce, encrypt);
        return new CallbackResponse(signature, time, nonce, encrypt);
    }

    /**
     * 构建指定长度的随机字符串
     *
     * @param count 要构建的字符串的长度
     * @return
     */
    private String getRandomStr(int count) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int randomMax = RANDOM_BASE_CHAR.length();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(randomMax);
            sb.append(RANDOM_BASE_CHAR.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 验证消息签名的正确性
     *
     * @param signature
     * @param token
     * @param time
     * @param noce
     * @param msg
     * @return
     */
    private boolean validMessage(String signature, String token, Long time, String noce, String msg) {
        return StringUtils.equals(signature, signature(token, String.valueOf(time), noce, msg));
    }

    private Event getEvent(String encrypt, String signature, Long timestamp, String nonce) throws Exception {
        if (validMessage(signature, callbackProperties.getToken(), timestamp, nonce, encrypt)) {
            try {
                EncryptMessage msg = EncryptMessage.from(aes.decrypt(encrypt));
                String msgBody = msg.getMsg();
                return om.readValue(msgBody, Event.class);
            } catch (Exception e) {
                log.error("解码回调请求体时发生错误", e);
                throw e;
            }
        } else {
            log.error("回调请求签名验证错误 encrypt={},signature={},timestamp={},nonce={}", encrypt, signature, timestamp, nonce);
            throw new RuntimeException("error for validate message");
        }
    }

    /**
     * 对消息进行签名
     *
     * @param token
     * @param time
     * @param noce
     * @param msg
     * @return
     */
    private String signature(String token, String time, String noce, String msg) {
        String[] all = new String[]{token, time, noce, msg};
        Arrays.sort(all);
        return DigestUtils.sha1Hex(StringUtils.join(all));
    }
}
