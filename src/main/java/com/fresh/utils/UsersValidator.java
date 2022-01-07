package com.fresh.utils;

import com.fresh.entity.Users;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UsersValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Users.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
