package com.fresh.bean;

import com.fresh.annotation.MComponent;
import org.springframework.stereotype.Component;

//@MComponent
@Component
public class MBean {

    public MBean() {
        System.out.println("MBean初始化......");
    }

    public String m() {
        return "OK";
    }

}
