package com.fresh.utils;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MiniCatch {

    String value() default "";

    //过期时间 ，秒
    long expire() default 0;
}
