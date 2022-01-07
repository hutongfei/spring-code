package com.fresh.utils;


import com.fresh.dao.CommonRepository;
import com.fresh.test.TUser;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

public class SQLUtils {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        CommonRepository cn = new CommonRepository();

        TUser tUser = new TUser();
//        tUser.setUserName("aaaa");
        tUser.setPassword("123");
        tUser.setState(1);
        tUser.setId(100l);
        tUser.setPrice(new BigDecimal(10));
        tUser.setCreateDate(new Date());
        tUser.setFlag(true);

        cn.save(tUser);

        cn.delete(tUser.getClass(), "1");

        cn.find(tUser);


    }
}
