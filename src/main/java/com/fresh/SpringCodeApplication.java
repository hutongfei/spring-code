package com.fresh;


import com.fresh.config.ThirdApiProperties;
import com.fresh.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;
import java.util.Enumeration;


@SpringBootApplication()
@EnableConfigurationProperties({ThirdApiProperties.class})
public class SpringCodeApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringCodeApplication.class, args);
//        SpringContextUtil contextUtil = context.getBean(SpringContextUtil.class);
//        contextUtil.setApplicationContext(context);

    }
}