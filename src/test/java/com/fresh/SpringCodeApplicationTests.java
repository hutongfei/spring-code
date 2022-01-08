package com.fresh;

import com.fresh.utils.RedissonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringCodeApplicationTests {

    @Autowired
    private RedissonUtil redissonUtil;

    @Test
    void contextLoads() throws InterruptedException {
    }

    private void count(int i,int max) {
        while (i < max) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println(i);
            i++;
        }
    }
}
