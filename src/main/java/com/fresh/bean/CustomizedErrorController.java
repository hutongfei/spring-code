package com.fresh.bean;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

//@Controller
//@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizedErrorController implements ErrorController {

    public String getErrorPath() {
        return null;
    }

    @RequestMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseBean error(HttpServletRequest request) {
        return ResponseBean.error(4, "not found");
    }
}