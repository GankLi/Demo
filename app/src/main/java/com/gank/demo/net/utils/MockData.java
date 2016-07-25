package com.gank.demo.net.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by GankLi on 2016/7/22.
 */

public class MockData {

    public static final String OP = "pf";
    public static final String OP_QUERY_DEVICE = "cx";
    public static final String OP_QUERY_ORDER = "cxdd";
    public static final String CONTENT = "woshishui";
    public static final String UMN = "ouo777";
    public static final String DNO = "160719911";
    public static final String MODE = "|0";
    public static final String MODE_QUERY_DEVICE = "cx";
    public static final String MODE_QUERY_ORDER = "cxdd";
    public static final String MSGNO = "990099";
    public static final String API_KEY = "0p6nl88vhpj000th";
    public static final String MD5_KEY = "aykj";
    public static final String KEY = md5(API_KEY + DNO + MD5_KEY);

    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

}
