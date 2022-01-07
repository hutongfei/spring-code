package com.fresh.listen;

import org.springframework.context.ApplicationEvent;

public class MyApiEvent extends ApplicationEvent {

    public MyApiEvent(Object source) {
        super(source);
    }
}
