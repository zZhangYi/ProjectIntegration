package com.zhy.exception.handler;

import com.zhy.exception.aspect.ExceptionAspect;
import com.zhy.exception.dto.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultVo<Object> handleException(Exception e) {
        String errorMsg = "";
        if (e instanceof NullPointerException) {
            errorMsg = "参数空指针异常";
        } else if (e instanceof HttpMessageNotReadableException) {
            errorMsg = "请求参数匹配错误," + e.getLocalizedMessage();
        } else {
            errorMsg = e.getMessage();
        }
        logger.error(String.format("请求异常[%s]", e));

        ResultVo<Object> resultVo = new ResultVo<>();
        resultVo.setResultCode("501");
        resultVo.setResultMsg(errorMsg);
        return resultVo;
    }
}
