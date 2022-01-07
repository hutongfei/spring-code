package com.fresh.utils;

import com.fresh.entity.Users;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class UsersEvents extends ApplicationEvent {

    private Users users;

    public UsersEvents(Object source) {
        super(source);
    }

    public UsersEvents(Object source, Users users) {
        super(source);
        this.users = users;
    }
}
