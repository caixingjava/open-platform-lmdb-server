package com.minivision.openplatform.lmdb.server.common.exception;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.minivision.openplatform.lmdb.server.common.response.RestResult;
import com.minivision.openplatform.lmdb.server.common.response.RestResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * <Description> <br>
 *
 * @author caixing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年07月13日 <br>
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    private RestResult runtimeExceptionHandler(Exception ex) {
        log.error(getStackTrace(ex.getCause()), ex);
        return RestResultBuilder.builder().failure().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private RestResult illegalParamsExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = Maps.newHashMapWithExpectedSize(bindingResult
                .getFieldErrorCount());
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("---------> invalid request! fields ex:{}", JSON.toJSONString(errors));
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST).data
                (errors).build();
    }

    @ExceptionHandler(BindException.class)
    public RestResult bindExceptionHandler(BindException ex) {
        Map<String, String> errors = Maps.newHashMapWithExpectedSize(ex.getFieldErrorCount());
        for (FieldError fieldError : ex.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("---------> invalid request! fields ex:{}", JSON.toJSONString(errors));
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST).data
                (errors).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RestResult messageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.warn("---------> json convert failure, exception:{}", ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public RestResult methodArgumentExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.error("---------> path variable, exception:{}", ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST)
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public RestResult illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.info("IllegalArgumentException exception:{}", ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST).build();
    }

    @ExceptionHandler(BusinessException.class)
    public RestResult businessExceptionHandler(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        if (ex.getErrorCode() == null) {
            log.warn("---------> business exception code:{}, message:{}",
                    ex.getCode(), ex.getMessage());
            return RestResultBuilder.builder().code(ex.getCode()).message(ex.getMessage()).build();
        }
        log.warn("---------> business exception errorCode code:{}, message:{}",
                errorCode.getErrorCode(), errorCode.getErrorMessage());
        return RestResultBuilder.builder().errorCode(ex.getErrorCode()).build();
    }

    @ExceptionHandler(ApplicationException.class)
    public RestResult applicationExceptionHandler(ApplicationException ex) {
        log.error("---------> application exception message:" + ex.getMessage(), ex);
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public RestResult noHandlerFoundException(NoHandlerFoundException ex) {
        log.warn("noHandlerFoundException 404 error requestUrl:{}, method:{}, exception:{}",
                ex.getRequestURL(), ex.getHttpMethod(), ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.NOT_FOUND).build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public RestResult httpRequestMethodHandler(HttpServletRequest request,
                                               HttpRequestMethodNotSupportedException ex) {
        log.warn("httpRequestMethodHandler 405 error requestUrl:{}, method:{}, exception:{}",
                request.getRequestURI(), ex.getMethod(), ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.METHOD_NOT_ALLOWED).build();
    }

    /**
     * get Throwable StackTrace.
     *
     * @param t
     * @return
     */
    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)){
            t.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return "";
        }
    }

}
