package com.fresh.external;

import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class MergedBeanDefinitionPostProcessorComponent implements MergedBeanDefinitionPostProcessor {
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> aClass, String s) {

    }

    @Override
    public void resetBeanDefinition(String beanName) {
        MergedBeanDefinitionPostProcessor.super.resetBeanDefinition(beanName);
    }
}
