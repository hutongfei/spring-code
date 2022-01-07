package com.fresh.listen;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyApiEventListen implements ApplicationListener<MyApiEvent> {

    @Override
    public void onApplicationEvent(MyApiEvent myApiEvent) {
        System.out.println("事件监听啦");
        System.out.println(myApiEvent.getSource());
    }
}
