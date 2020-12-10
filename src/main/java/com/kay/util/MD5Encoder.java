package com.kay.util;

import java.security.MessageDigest;

public class MD5Encoder {

    private String salt;

    public MD5Encoder(String salt) {
        this.salt = salt;
    }

    public String md5EncodeUtf8(String origin) {
        origin = origin + this.salt;
        return md5Encode(origin, "utf-8");
    }

    private static String byteArrayToHexString(byte[] data) {
        StringBuilder resultSb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            resultSb.append(byteToHexString(data[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static String md5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception exception) {
            //ingore
        }
        return resultString.toUpperCase();
    }

    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

}
