package com.fresh.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

public class JSSDKSignatureUtils {
    // 签名
    public static void main(String[] args) {
        String  x = System.currentTimeMillis() + "";
        System.out.println("1637388501".length());
//        String t1 = "LIKLckvwlJT9cWIhEQTwfGb7TY_FkSut_q6F24ifV3QxQfo-0X3i_hqPY7ZYkf7YHFMWlfQP4PGbXl4nc9pBMw";
//        String t2 = "LIKLckvwlJT9cWIhEQTwfGb7TY_FkSut_q6F24ifV3TD58HkWmntVz6vhKaQPpgApNs3lt0h12yZujM-B4lG0";
//        String t3 = "LIKLckvwlJT9cWIhEQTwfGb7TY_FkSut_q6F24ifV3SnERmxWjAJqjl3BN3XbtqmSs1aPqP_Z93OILb8f1Q6VA";
//        String t4 = "LIKLckvwlJT9cWIhEQTwfGb7TY_FkSut_q6F24ifV3Q1K29i-InmW-uT6Ly7wd3o2YXZ5O3kknvs3dxjzh3Cgw";
//        String sign1 = sign("wx68db619a796d94c6",t1, "https://4swd.cn/", "8b053d3afbd046979ab0bbbf6cd40a1b", "1637388501");
//        String sign2 = sign("wx68db619a796d94c6",t2, "https://4swd.cn/", "8b053d3afbd046979ab0bbbf6cd40a1b", "1637388501");
//        String sign3 = sign("wx68db619a796d94c6",t3, "https://4swd.cn/", "8b053d3afbd046979ab0bbbf6cd40a1b", "1637388501");
//        String sign4 = sign("wx68db619a796d94c6",t4, "https://4swd.cn/", "8b053d3afbd046979ab0bbbf6cd40a1b", "1637388501");
//        System.out.println(sign1);
//        System.out.println(sign2);
//        System.out.println(sign3);
//        System.out.println(sign4);
//
//
    }

    public static String sign(String appId,String jsapi_ticket, String url, String nonce_str, String timestamp) {
        JSONObject jsonObject = new JSONObject();
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        String string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        jsonObject.put("url", url);
        jsonObject.put("appId", appId);
        jsonObject.put("jsapi_ticket", jsapi_ticket);
        jsonObject.put("nonceStr", nonce_str);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("signature", signature);

        return jsonObject.toJSONString();
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }



}
