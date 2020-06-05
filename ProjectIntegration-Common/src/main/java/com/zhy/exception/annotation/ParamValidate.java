package com.zhy.exception.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异常处理切面 非空校验
 * 在需要校验的地方controller上加上@ParamValidate，入参加上@Valid
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParamValidate {
    String value() default "";
}
