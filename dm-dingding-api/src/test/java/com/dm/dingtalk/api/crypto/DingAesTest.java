package com.dm.dingtalk.api.crypto;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.dm.dingtalk.api.callback.EncryptMessage;
import com.dm.dingtalk.api.crypto.DingAes;

public class DingAesTest {

    @Test
    public void decrypt() {
        DingAes as = new DingAes("xxxxxxxxlvdhntotr3x9qhlbytb18zyz5zxxxxxxxxx");
        try {
            String msg = "qEf4v+/MuYM4sszppgXYz+6hIOwKR3Rfyl1lP+ynhMQoBhGtIY1bvp1uAiiAW4t7HOwnUV6FREmuFwYCidkjnw==";
            byte[] bytes = as.decrypt(msg);
            EncryptMessage em = EncryptMessage.from(bytes);
            System.out.println(em);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void encrypt() {
        DingAes aes = new DingAes("xxxxxxxxlvdhntotr3x9qhlbytb18zyz5zxxxxxxxxx");
        try {
            String nonce = "1234567890123456";
            EncryptMessage msg = EncryptMessage.of(nonce, "success", "ding1486af70d528a66635c2f4657eb6378f");
            System.out.println(aes.encryptToBase64String(msg.toBytes()));
        } catch (Exception e) {
        }

    }

}
