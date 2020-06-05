package com.zhy.mongo.aspect;

import com.zhy.enums.SystemTypeEnum;
import com.zhy.mongo.annotation.SyncSaveMsg;
import com.zhy.mongo.core.MongoClientService;
import com.zhy.mongo.dto.MongoEntity;
import com.zhy.mongo.enums.MsgTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import java.lang.reflect.Method;

/**
 * 【类或接口功能描述】
 * 同步保存日志拦截器
 *
 * @version 1.0.0
 */
@Component
@Aspect
public class SyncSaveMsgInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SyncSaveMsgInterceptor.class);

    private static final String SEP_SPOT = ".";

    @Autowired
    private MongoClientService mongoClientService;
    
    /**
     * 【类或接口功能描述】
     * 切点（配置了注解AsynSaveMsg注解的所有方法）
     *
     * @author huanaoyun
     */
    @Pointcut("@annotation(com.zhy.mongo.annotation.SyncSaveMsg)")
    public void addAdvice() {}

    /**
     * 【类或接口功能描述】
     * 保存添加保存日志注解方法的出入参
     *
     * @author huanaoyun
     * @param proceedingJoinPoint-
     * @return Object
     */
    @Around("addAdvice()")
    public Object invoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    	// 获取添加了注解的方法
    	MethodSignature ms = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = ms.getMethod();
        // 获取注解参数值
        SyncSaveMsg syncSaveMsg = method.getAnnotation(SyncSaveMsg.class);
        SystemTypeEnum sysTypeEnum = syncSaveMsg.sysType();
        String bizType = syncSaveMsg.bizType();
        String seqNo = syncSaveMsg.seqNo();
        String bizNo = syncSaveMsg.bizNo();
        // 获取方法入参
        Object[] args = proceedingJoinPoint.getArgs();
        // 默认取traceId
        if(StringUtils.isBlank(seqNo)){
            seqNo = MDC.get("traceId");
        }
        // 默认取traceId
        if (StringUtils.isBlank(bizNo)) {
            bizNo = getBizNo(bizNo, method, args);
        }
        // 若方法入参不为空则保存
        if (args != null && args.length > 0) {
            try {
                Object[] reqArr = new Object[args.length];
                for (int i=0;i < args.length;i++ ) {
                    if (!(args[i] instanceof ServletRequest)) {
                        reqArr[i] = args[i];
                    }
                }
                mongoClientService.save(new MongoEntity<>(seqNo, bizNo, MsgTypeEnum.REQ.name(), reqArr), initCollectionName(sysTypeEnum, bizType));
            } catch (Exception e) {
                logger.warn("asynchronized save request message failed, method: {}, sequence: {}\n{}", method.getName(), seqNo, e);
            }
        }
        // 执行方法
        Object result = proceedingJoinPoint.proceed();
        // 若方法出参不为空则保存
        if (result != null) {
            try {
                mongoClientService.save(new MongoEntity<>(seqNo, bizNo, MsgTypeEnum.RES.name(), result), initCollectionName(sysTypeEnum, bizType));
            } catch (Exception e) {
                logger.warn("asynchronized save response message failed, method: {}, sequence: {}\n{}", method.getName(), seqNo, e);
            }
        }
        return result;
    }

    /**
     * 组装集合名称
     *
     * @param sysTypeEnum 系统编码
     * @param bizType 业务类型
     * @return String 集合名称
     */
    private String initCollectionName(SystemTypeEnum sysTypeEnum, String bizType) {
        return sysTypeEnum.getValue() + SEP_SPOT + bizType;
    }

    /**
     *
     *
     * @date 2019/11/9 18:52
     * @author ex-lixianglei
     * @param bizNo spel表达式
     * @return 解析后的表达式
     *
     */
    private String getBizNo(String bizNo, Method method, Object[] args) {
        if (StringUtils.isEmpty(bizNo)) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        try {
            //获取被拦截方法参数名列表(使用Spring支持类库)
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paraNameArr = u.getParameterNames(method);
            //SPEL上下文
            StandardEvaluationContext context = new StandardEvaluationContext();
            //使用SPEL进行key的解析
            ExpressionParser parser = new SpelExpressionParser();
            //把方法参数放入SPEL上下文中
            for (int i = 0; i < paraNameArr.length; i++) {
                // 不支持在ServletRequest取值
                if (!(args[i] instanceof ServletRequest)) {
                    context.setVariable(paraNameArr[i], args[i]);
                }
            }
            return parser.parseExpression(bizNo).getValue(context, String.class);
        } catch (Exception e) {
            logger.error("Spel表达式执行出错：spel：{},errormessage:{}", bizNo, e.getMessage());
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Spel表达式执行耗时：{}", (System.currentTimeMillis() - startTime));
            }
        }
        return null;
    }
}
