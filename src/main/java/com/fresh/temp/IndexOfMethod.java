package com.fresh.temp;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import javax.annotation.PostConstruct;
import java.util.List;

public class IndexOfMethod implements TemplateMethodModel {


    @Override
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 2) {
            throw new TemplateModelException("Wrong arguments");
        }
        return new SimpleNumber(
                ((String) args.get(1)).indexOf((String) args.get(0))
        );
    }
}
