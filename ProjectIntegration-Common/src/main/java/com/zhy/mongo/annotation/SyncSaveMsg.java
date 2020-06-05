package com.zhy.mongo.annotation;

import com.zhy.enums.SystemTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 【类或接口功能描述】
 * 同步保存日志
 *
 * @version 1.0.0
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SyncSaveMsg {

    /**
     * 系统类型
     */
    SystemTypeEnum sysType();

    /**
     * 业务类型
     */
    String bizType();

    /**
     * 流水号
     */
    String seqNo() default "";

    /**
     * 业务号spel表达式
     */
    String bizNo() default "";
}
