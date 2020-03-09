package com.dm.dingtalk.api.callback;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dm.dingtalk.api.callback.model.CallbackProperties;
import com.dm.dingtalk.api.callback.model.CallbackRequest;
import com.dm.dingtalk.api.callback.model.CallbackResponse;
import com.dm.dingtalk.api.callback.model.EncryptMessage;
import com.dm.dingtalk.api.callback.model.Event;
import com.dm.dingtalk.api.crypto.AES;
import com.dm.dingtalk.api.model.DingClientConfig;
import com.dm.dingtalk.api.service.DingTalkService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("dingTalk/callback")
public class CallbackController {

    private Map<String, Consumer<Event>> handlers;

    private CallbackProperties callbackProperties;

    private DingClientConfig config;

    private ObjectMapper om;

    private AES aes;

    private static final Integer RANDOM_LENGTH = 6;

    @Autowired
    private DingTalkService dingService;

    public void setCallbackProperties(CallbackProperties callbackProperties) {
        this.callbackProperties = callbackProperties;
        this.aes = new AES(callbackProperties.getAesKey());
    }

    public void setHandlers(Map<String, Consumer<Event>> handlers) {
        this.handlers = handlers;
    }

    public void setObjectMapper(ObjectMapper om) {
        this.om = om;
    }

    public void setDingClientConfig(DingClientConfig config) {
        this.config = config;
    }

    @PostMapping(params = { "signature" })
    public ResponseEntity<?> apply(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") Long timestamp,
            @RequestParam("nonce") String nonce,
            @RequestBody CallbackRequest rsp)
            throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String encrypt = rsp.getEncrypt();
        try {
            Event event = getEvent(encrypt, signature, timestamp, nonce);
            String eventType = event.getType();
            System.out.println(event);
            if ("check_url".equals(eventType)) {
                return ResponseEntity.ok(okResponse());
            } else if (MapUtils.isNotEmpty(handlers)) {
                System.out.println(handlers);
//                  Consumer<Event> consumer = handlers.get(eventType);
//                  if (!Objects.isNull(consumer)) {
//                      consumer.accept(event);
//                  }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(okResponse());
    }

    public CallbackResponse okResponse()
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String time = String.valueOf(ZonedDateTime.now().toEpochSecond());
        String nonce = getRandomStr(RANDOM_LENGTH);
        EncryptMessage msg = EncryptMessage.of(nonce, "success", config.getCorpId());
        String encrypt = aes.encryptToBase64String(msg.toBytes());
        String signature = signature(callbackProperties.getToken(), time, nonce, encrypt);
        return new CallbackResponse(signature, time, nonce, encrypt);
    }

    private boolean validMessage(String signature, String token, Long time, String noce, String msg) {
        String computedSignature = signature(token, String.valueOf(time), noce, msg);
        return StringUtils.equals(signature, computedSignature);
    }

    /**
     * 注册钉钉回调地址
     * 
     * @return
     */
    @PostMapping
    public String registry() {
        return dingService.registryCallback(callbackProperties);
    }

    /**
     * 取消回调注册
     * 
     * @return
     */
    @DeleteMapping
    public String delete() {
        return dingService.deleteCallback();
    }

    @GetMapping(params = "error")
    public String getCallbackError() {
        return dingService.getFailureCallback();
    }

    public String getRandomStr(int count) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    private Event getEvent(String encrypt, String signature, Long timestamp, String nonce) throws Exception {
        try {
            boolean b = validMessage(signature, callbackProperties.getToken(), timestamp, nonce, encrypt);
            if (b) {
                EncryptMessage msg = EncryptMessage.from(aes.decrypt(encrypt));
                String msgBody = msg.getMsg();
                Event event = om.readValue(msgBody, Event.class);
                return event;
            } else {
                throw new RuntimeException("error for validate message");
            }
        } catch (Exception e) {
            throw e;
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
        String[] all = new String[] { token, time, noce, msg };
        Arrays.sort(all);
        return DigestUtils.sha1Hex(StringUtils.join(all));
    }
}
