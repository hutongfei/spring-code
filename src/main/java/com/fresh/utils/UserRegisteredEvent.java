package com.fresh.utils;

import com.fresh.entity.Users;
import org.apache.tomcat.jni.User;

public class UserRegisteredEvent extends UsersEvents {


    public UserRegisteredEvent(Object source, Users users) {
        super(source, users);
    }
}
