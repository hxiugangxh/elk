package com.ylz.log.elk.base.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    OK(0, "成功"),
    FAIL(1, "异常");

    private Integer code;
    private String msg;

    ExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
