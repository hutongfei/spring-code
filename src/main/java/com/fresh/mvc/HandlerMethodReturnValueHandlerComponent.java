package com.fresh.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;

//@Component
public class HandlerMethodReturnValueHandlerComponent implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        modelAndViewContainer.setRequestHandled(true);
        nativeWebRequest
                .getNativeResponse(HttpServletResponse.class)
                .getWriter()
                .write(JSON.toJSONString(o));
    }
}
