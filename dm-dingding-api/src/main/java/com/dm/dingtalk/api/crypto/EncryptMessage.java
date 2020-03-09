package com.dm.dingtalk.api.crypto;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

/**
 * 加密消息
 * 
 * @author ldwqh0@outlook.com
 *
 */
public class EncryptMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String random;
    private int msgLength;
    private String msg;
    private String key;

    private final String CHARSET = "UTF-8";

    /**
     * 从byte[]中转换一个消息
     * 
     * @param bytes
     * @return
     */
    public static EncryptMessage from(byte[] bytes) {
        try {
            return new EncryptMessage(bytes);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static EncryptMessage from(String base64Str) {
        return from(Base64.getDecoder().decode(base64Str));
    }

    public static EncryptMessage of(String random, String msg, String key) {
        try {
            EncryptMessage r = new EncryptMessage();
            r.key = key;
            r.msg = msg;
            r.random = random;
            r.msgLength = msg.getBytes("UTF-8").length;
            return r;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public byte[] toBytes() {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            byte[] randomb = random.getBytes(CHARSET);
            byte[] msgb = msg.getBytes(CHARSET);
            byte[] lengthb = intToByteArray(msgb.length);
            byte[] keyb = key.getBytes(CHARSET);
            byteStream.write(randomb);
            if (randomb.length < 16) {
                byteStream.write(new byte[16 - randomb.length]);
            }
            byteStream.write(lengthb);
            byteStream.write(msgb);
            byteStream.write(keyb);
            return byteStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    private EncryptMessage(byte[] bytes) throws UnsupportedEncodingException {
        random = new String(Arrays.copyOfRange(bytes, 0, 16), "UTF-8");
        msgLength = toInt(Arrays.copyOfRange(bytes, 16, 20));
        msg = new String(Arrays.copyOfRange(bytes, 20, 20 + msgLength), "UTF-8");
        key = new String(Arrays.copyOfRange(bytes, 20 + msgLength, bytes.length), "UTF-8");
    }

    private EncryptMessage() {
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private int toInt(byte[] bytes) {
        int num = bytes[0];
        for (int i = 0; i < 4 && i < bytes.length; i++) {
            num = (num << 8) | (bytes[i] & 255);
        }
        return num;
    }

    private byte[] intToByteArray(int num) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (num >>> ((3 - i) * 8));
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "EncryptMessage [random=" + random + ", msgLength=" + msgLength + ", msg=" + msg + ", key=" + key + "]";
    }

}
