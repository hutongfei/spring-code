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

        new Thread(() -> {
            redissonUtil.lock("lock");
            int i = 0;
            count(i,10);
            redissonUtil.unlock("lock");
            count(i,20);
        }, "计时").start();

        TimeUnit.SECONDS.sleep(1);
        while (true) {
            redissonUtil.lock("lock",TimeUnit.SECONDS,5);
            System.out.println("尝试5s 结束");
            break;
        }


        redissonUtil.unlock("lock");
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
