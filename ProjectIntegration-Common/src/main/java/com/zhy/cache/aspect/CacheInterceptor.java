package com.zhy.cache.aspect;


import com.zhy.cache.annotation.PushCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Aop切面编程
 */
@Component
@Aspect
public class CacheInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    // 环绕通知 ： 监控自定义注解 。　
    @Around("@annotation(com.zhy.cache.annotation.PushCache)")
    public Object doAnyThing(ProceedingJoinPoint joinPoint) throws Throwable {
        String key =null;
        //1.反射技术：从注解里里面，读取key的生成规则。
        //1.1 从切入点，获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //1.2 从切点，获取目标对象的字节码，的方法。参数：（方法名，方法的所有参数）。
        Method method = joinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getMethod().getParameterTypes());
        //1.3 从方法获取注解。
        PushCache annotation = method.getAnnotation(PushCache.class);
        //1.4 从注解，获取注解信息。'SkuQueryService_findById' + #id
        String keyEL = annotation.key();


        //2. 创建 springEL表达式 解析器
        SpelExpressionParser parser = new SpelExpressionParser();
        // 解析器 获取指定表达式  'SkuQueryService_findById' + #id     的表达式对象
        Expression expression = parser.parseExpression(keyEL);
        // 设置解析上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //2.1 创建默认参数名 发现者
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        //2.2 获取方法中的所有参数名。
        String[] parameterNames = discoverer.getParameterNames(method);
        //2.3 获取切点方法中的所有参数值。
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            // 把参数名，参数值，设置到解析器上下文
            context.setVariable(parameterNames[i],args[i].toString());
        }
        //表达式 匹配 解析上下文 中的内容 ,拿到key
        key = expression.getValue(context).toString();

        Object o = redisTemplate.opsForValue().get(key);
        if (o !=null){
            // 缓存中有数据，直接返回。
            //查询缓存
            System.out.println("查询缓存返回。");
            //延迟缓存失效时间，1天。
            redisTemplate.expire(key,1, TimeUnit.DAYS);
            return o;
        }
        // 缓存穿透标记
        Object penetrateFlag =  redisTemplate.opsForValue().get(key + "penetrateFlag");
        if (null == penetrateFlag){
            // 没有防止 缓存穿透标记，查数据库。

            // 执行切点 中的代码，查询数据库。
            Object proceed = joinPoint.proceed();

            if (null == proceed){
                // 数据库数据为空，设置缓存穿透标记。15分钟
                redisTemplate.opsForValue().set(key + "penetrateFlag",true,15,TimeUnit.MINUTES);
            }else {
                // 数据库数据不为空，把数据存到缓存中。缓存1天。
                redisTemplate.opsForValue().set(key,proceed,1, TimeUnit.DAYS);
            }
            return  proceed ;
        }
        // 延迟缓存失效时间  15分钟 缓存穿透标记
        redisTemplate.expire(key+"penetrateFlag",15,TimeUnit.MINUTES);
        // 返回
        return null;
    }

}
