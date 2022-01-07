package com.fresh.bean;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class CustomInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        if (request.getRequestURI().contains("haha")) {
            throw new RuntimeException("自定义异常哈哈哈哈");
//            return false;
        }
        return true;
    }
}
