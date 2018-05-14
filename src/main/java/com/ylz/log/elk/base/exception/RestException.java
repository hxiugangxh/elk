package com.ylz.log.elk.base.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义统一异常类
 * Updated by linds on 2017/10/16.
 */
public class RestException extends RuntimeException {

    private final static Logger logger = LoggerFactory.getLogger(RestException.class);

    private Integer code = 1;

    public RestException(Integer code) {
        this.code = code;
    }

    public RestException(String msg) {
        super(msg);
    }

    public RestException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public RestException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMsg());
        logger.info("【系统异常】{}" + exceptionCode.getMsg());
        this.code = exceptionCode.getCode();
    }


    public RestException(ExceptionCode exceptionCode, String message) {
        super(exceptionCode.getMsg() + ":" + message);
        logger.info("【系统异常】{}" + message);
        this.code = exceptionCode.getCode();
    }


    public RestException(ExceptionCode exceptionCode, Throwable e) {
        super(exceptionCode.getMsg(), e);
        logger.info("【系统异常】{}" + e.getMessage());
        this.code = exceptionCode.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}