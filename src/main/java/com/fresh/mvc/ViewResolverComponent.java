package com.fresh.mvc;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.Locale;

/**
 * 视图解析器
 */
@Component
public class ViewResolverComponent implements ViewResolver {

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return null;
    }
}
