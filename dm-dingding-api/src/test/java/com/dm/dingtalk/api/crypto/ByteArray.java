package com.dm.dingtalk.api.crypto;

import org.junit.jupiter.api.Test;

public class ByteArray {

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
            bytes[i] = (byte) ((num >>> ((3 - i) * 8)) & 0xff);
        }
        return bytes;
    }

    @Test
    public void tt() {
        System.out.println(Integer.MIN_VALUE);
        byte[] bytes = intToByteArray(Integer.MIN_VALUE);
        System.out.println(toInt(bytes));
    }
}
