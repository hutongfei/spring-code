package com.fresh.service;

import com.fresh.entity.Users;
import com.fresh.utils.MiniCatch;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UsersService {

    private Object object = new Object();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    public void insert(Users users) {
        ThreadLocal<Object> objectThreadLocal = new ThreadLocal<>();
        Object o = objectThreadLocal.get();
        jdbcTemplate.update("UPDATE `users` SET `name` = ? WHERE `id` = ?", new Object[]{users.getName(), users.getId()});
//            new Thread(() -> {
        throw new RuntimeException("报错啦");
//            }).start();

    }

    @MiniCatch(value = "${id}",expire = 10)
    public Users getUsers(Long id) {
        String sqlValue = String.format("select * from users where id = %s limit 1 ",id);
        Map<String, Object> hmp = jdbcTemplate.queryForMap(sqlValue);
        if (hmp.isEmpty()) return null;

        Users users = new Users();
        users.setId(hmp.get("id") == null ? null : (Integer) hmp.get("id"));
        users.setSex(hmp.get("sex") == null ? null : (String) hmp.get("sex"));
        users.setName(hmp.get("name") == null ? null : (String) hmp.get("name"));
        return users;
    }
}
