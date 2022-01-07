package com.fresh.bean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;

//@RestControllerAdvice
public class GlobalExceptionHandler {
    // 捕获所有 Exception，一定要加上，阻断默认异常处理器传递
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseBean handleException(Exception e) {
        System.err.println(e.toString());
        e.printStackTrace();
        return ResponseBean.error(3, e.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseBean handleServletException(ServletException ex) {
        return ResponseBean.error(1, "servlet exception");
    }

    @ExceptionHandler
    public ResponseEntity<ResponseBean> handleSpecialException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatus()).body(ResponseBean.error(2, e.getReason()));
    }
}