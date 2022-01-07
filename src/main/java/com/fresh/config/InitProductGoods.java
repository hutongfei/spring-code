package com.fresh.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Bean 初始化完成后 回调接口
 */
@Component
public class InitProductGoods implements InitializingBean {



    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
