package com.fresh.extend;

import com.fresh.annotation.MAutowired;
import com.fresh.bean.MBean;
import com.sun.media.sound.SoftTuning;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class BeanPostProcess implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(MAutowired.class)) {
                Class<?> aClass1 = declaredField.getType();
                // 获取字段对应的bean
                MBean bean1 = context.getBean(MBean.class);
                System.out.println(bean1);
                Object instance = context.getBean(aClass1.getSimpleName(),aClass1);

                declaredField.setAccessible(true);
                try {
                    if (instance != null) {
                        declaredField.set(bean, instance);// 当前bean 的字段设置值
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
