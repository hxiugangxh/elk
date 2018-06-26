package com.ylz.log.elk.base.exception;

import com.ylz.log.elk.base.result.MyFieldError;
import com.ylz.log.elk.base.result.RestResult;
import com.ylz.log.elk.base.result.RestResultGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collection;

/**
 * 全局统一异常处理,对特定的几个异常进行捕获及处理，这里不处理的，会转到ErrorController进行处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 内部代码抛出错误统一捕获处理
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult handleServerException(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof RestException) {
            RestException resourceException = (RestException) ex;
            return RestResultGenerator.createResult(resourceException.getCode(),
                    resourceException.getMessage(), null);
        } else {
            return RestResultGenerator.createFailResult("服务器内部错误，" + ex.getMessage());
        }
    }

    /**
     * 请求参数错误
     **/
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public RestResult IllegalArgumentException(Exception ex) {
        ex.printStackTrace();
        return RestResultGenerator.createFailResult(ex.getMessage());
    }


    /**
     * 1.HttpMessageNotReadableException：处理post 请求时参数类型错误
     * 2.MethodArgumentTypeMismatchException：处理get 请求时参数类型错误
     **/
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException
            .class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestResult parameterTypeErrorException(Exception e) {
        e.printStackTrace();
        return RestResultGenerator.createFailResult("参数类型错误");
    }

    /**
     * GET请求时，缺少必须参数（参数形式为？号后面的一串，缺少路径上的参数时，匹配到404）
     **/
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestResult missingParameterException(MissingServletRequestParameterException e) {
        e.printStackTrace();
        return RestResultGenerator.createFailResult(e.getMessage());
    }

    /**
     * 字段校验错误统一捕获处理
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestResult validateErrorHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            Collection<MyFieldError> collect = CollectionUtils.collect(bindingResult
                    .getFieldErrors(), new Transformer<FieldError, MyFieldError>() {

                public MyFieldError transform(FieldError o) {
                    MyFieldError fieldError = new MyFieldError();
                    fieldError.setField(o.getField());
                    fieldError.setMsg(o.getDefaultMessage());
                    return fieldError;
                }
            });
            return RestResultGenerator.createFailResult("字段校验失败", collect);
        } else {
            return RestResultGenerator.createFailResult("字段校验失败");
        }
    }

}
