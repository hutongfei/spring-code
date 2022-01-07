package com.fresh.config;

import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

@Configuration
public class FreeMarkerVariableConfiguration {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void setVariableConfiguration() throws TemplateModelException {
        configuration.setSharedVariable("base", servletContext.getContextPath());
    }
}
