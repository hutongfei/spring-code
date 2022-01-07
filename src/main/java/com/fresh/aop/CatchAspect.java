package com.fresh.aop;

import com.alibaba.fastjson.JSONObject;
import com.fresh.utils.MiniCatch;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

@EnableAspectJAutoProxy
@Component
@Aspect
public class CatchAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut(value = "@annotation(com.fresh.utils.MiniCatch)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void beginTransaction() {
//        System.out.println("before beginTransaction");
    }


    @AfterReturning(value = "pointcut()", returning = "returnObject")
    public void afterReturning(JoinPoint joinPoint, Object returnObject) {
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object value = getCatchValue(joinPoint);
            if (value != null) return value;


            Object proceed = joinPoint.proceed();


            value = getCatchValue(joinPoint);
            if (value == null && proceed != null) putCatchValue(joinPoint, proceed);

            return proceed;
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("around");
        }
    }

    //缓存数据
    private void putCatchValue(ProceedingJoinPoint joinPoint, Object proceed) {
        if (proceed == null) return;
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String jsonResult = JSONObject.toJSONString(proceed);

        MiniCatch annotation = method.getAnnotation(MiniCatch.class);
        if (annotation != null) {
            String key = StringUtils.strip(annotation.value(), "${}");
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (StringUtils.equals(parameter.getName(), key)) {
                    Object[] args = joinPoint.getArgs();
                    System.out.println(args +  " 插入缓存 " + jsonResult);
//                    redisTemplate.opsForValue().set(String.valueOf(args[i]), jsonResult);
                    redisTemplate.opsForValue().set(String.valueOf(args[i]), jsonResult,annotation.expire(), TimeUnit.SECONDS);
                }

            }
        }

    }

    // 缓存中获取数据
    private Object getCatchValue(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        MiniCatch annotation = method.getAnnotation(MiniCatch.class);
        if (annotation != null) {
            String key = StringUtils.strip(annotation.value(), "${}");
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (StringUtils.equals(parameter.getName(), key)) {
                    Object arg = joinPoint.getArgs()[i];
                    if (arg == null) return null;
                    Object resultValue = redisTemplate.opsForValue().get(String.valueOf(arg));
                    if (resultValue != null) {
                        Class<?> returnType = method.getReturnType();
                        System.out.println(arg +  "  获取缓存" + resultValue);
                        return JSONObject.parseObject(String.valueOf(resultValue), returnType);
                    }
                }
            }
        }
        return null;
    }

}
