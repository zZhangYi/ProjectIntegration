package com.zhy.exception.Validator;

/**
 * 参数校验异常类
 *
 * @description ParamsCheckException
 */
public class ParamsCheckException extends RuntimeException {

    private int code;

    private String message;

    private Throwable cause;

    public ParamsCheckException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ParamsCheckException(int code, String message, Throwable cause) {
        this.code = code;
        this.message = message;
        this.cause = cause;
    }

    public ParamsCheckException(int code, Throwable cause) {
        this.code = code;
        this.cause = cause;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
