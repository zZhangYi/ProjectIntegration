package com.zhy.cache.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 注解的生命周期：运行时
@Retention(RetentionPolicy.RUNTIME)
// 注解的应用范围：修饰方法
@Target(ElementType.METHOD)
public @interface PushCache {

    /**
     * key 的生成规则，通过springEL表达式
     * @return
     */
    String key();

}
