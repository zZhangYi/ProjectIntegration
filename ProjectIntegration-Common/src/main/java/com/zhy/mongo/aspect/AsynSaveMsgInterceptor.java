package com.zhy.mongo.aspect;

import com.zhy.enums.SystemTypeEnum;
import com.zhy.mongo.annotation.AsynSaveMsg;
import com.zhy.mongo.enums.MsgTypeEnum;
import com.zhy.mongo.eventbus.AsynSaveMsgDTO;
import com.zhy.mongo.eventbus.SaveMsgEventSubscribe;
import com.zhy.utils.EventUtil;
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
 * 异步保存日志拦截器
 *
 * @version 1.0.0
 */
@Component
@Aspect
public class AsynSaveMsgInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AsynSaveMsgInterceptor.class);

    @Autowired
    private SaveMsgEventSubscribe saveMsgEventSubscribe;

    /**
     * 【类或接口功能描述】
     * 切点（配置了注解AsynSaveMsg注解的所有方法）
     */
    @Pointcut("@annotation(com.zhy.mongo.annotation.AsynSaveMsg)")
    public void addAdvice() {
    }

    /**
     * 【类或接口功能描述】
     * 异步保存添加保存日志注解方法的出入参（通过EventBus）
     *
     * @param proceedingJoinPoint -
     * @return Object
     */
    @Around("addAdvice()")
    public Object invoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取添加了注解的方法
        MethodSignature ms = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = ms.getMethod();
        // 获取注解参数值
        AsynSaveMsg asynSaveMsg = method.getAnnotation(AsynSaveMsg.class);
        SystemTypeEnum sysTypeEnum = asynSaveMsg.sysType();
        String bizType = asynSaveMsg.bizType();
        String seqNo = asynSaveMsg.seqNo();
        String bizNo = asynSaveMsg.bizNo();
        // 获取方法入参
        Object[] args = proceedingJoinPoint.getArgs();
        // 判断注解参数，若为空则直接执行方法
        if (StringUtils.isBlank(bizType)) {
            return proceedingJoinPoint.proceed();
        }
        // 默认取traceId
        if (StringUtils.isBlank(seqNo)) {
            seqNo = MDC.get("traceId");
        }
        if (StringUtils.isNotEmpty(bizNo)) {
            bizNo = getBizNo(bizNo, method, args);
        }
        EventUtil.regist(saveMsgEventSubscribe);
        // 若方法入参不为空则广播到对应的日志保存订阅者
        if (args != null && args.length > 0) {
            try {
                Object[] reqArr = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (!(args[i] instanceof ServletRequest)) {
                        reqArr[i] = args[i];
                    }
                }
                EventUtil.post(initSaveLogDTO(sysTypeEnum, bizType, seqNo, reqArr, MsgTypeEnum.REQ, bizNo));
            } catch (Exception e) {
                logger.error("asynchronized save request message failed, method: {}, sequence: {}\n{}", method.getName(), seqNo, e);
            }
        }
        // 执行方法
        Object result = proceedingJoinPoint.proceed();
        // 若方法出参不为空则广播到对应的日志保存订阅者
        if (result != null) {
            try {
                EventUtil.post(initSaveLogDTO(sysTypeEnum, bizType, seqNo, result, MsgTypeEnum.RES, bizNo));
            } catch (Exception e) {
                logger.error("asynchronized save response message failed, method: {}, sequence: {}\n{}", method.getName(), seqNo, e);
            }
        }
        return result;
    }

    /**
     * 【类或接口功能描述】
     * 保存添加保存日志注解方法的出入参
     *
     * @param sysTypeEnum 系統类型
     * @param bizType     业务类型
     * @param seqNo       流水号
     * @param data        业务数据
     * @param msgTypeEnum 信息类型
     * @param bizNo       业务号
     * @return AsynSaveMsgDTO
     */
    private AsynSaveMsgDTO initSaveLogDTO(SystemTypeEnum sysTypeEnum, String bizType, String seqNo,
                                          Object data, MsgTypeEnum msgTypeEnum, String bizNo) {
        AsynSaveMsgDTO asynSaveMsgDTO = new AsynSaveMsgDTO();
        asynSaveMsgDTO.setSystemTypeEnum(sysTypeEnum);
        asynSaveMsgDTO.setBusinessType(bizType);
        asynSaveMsgDTO.setSeqNo(seqNo);
        asynSaveMsgDTO.setData(data);
        asynSaveMsgDTO.setMsgTypeEnum(msgTypeEnum);
        asynSaveMsgDTO.setBizNo(bizNo);
        return asynSaveMsgDTO;
    }

    /**
     * @param bizNo spel表达式
     * @return 解析后的表达式
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
            //使用SPEL进行key的解析
            ExpressionParser parser = new SpelExpressionParser();
            //SPEL上下文
            StandardEvaluationContext context = new StandardEvaluationContext();
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
