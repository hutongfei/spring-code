package com.fresh.external;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

public class HandlerMappingComponent implements HandlerMapping {
    @Override
    public boolean usesPathPatterns() {
        return HandlerMapping.super.usesPathPatterns();
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }
}
