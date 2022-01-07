package com.fresh.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    // 是否开启地址查询
    private static boolean isAddressEnabled = true;

    public static String getAddress(String ip) {
        if (isAddressEnabled) {
            try {
                String str = HttpUtil.get(IP_URL+"?ip="+ip +"&json=true");
                if (StringUtils.isNotBlank(str)) {
                    String json = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
                    JSONObject obj = JSONObject.parseObject(json);
                    String region = obj.getString("pro");
                    String city = obj.getString("city");
                    return String.format("%s %s", region, city);
                }
            } catch (Exception e) {
                return "IP 获取地址异常"+e.getMessage();
            }
        }
        return null;
    }
}
