package com.minivision.openplatform.lmdb.server.common.response;


import com.minivision.openplatform.lmdb.server.common.exception.ErrorCode;
import com.minivision.openplatform.lmdb.server.common.exception.GlobalErrorCode;

/**
 * RestResult Builder
 * <p>
 * Created by liangliang on 2017/02/16.
 */
public class RestResultBuilder<T> {

    protected int timeused;

    protected int code;

    protected String message;

    protected T data;

    public static RestResultBuilder builder() {
        RestResultBuilder restResultBuilder = new RestResultBuilder();
        return restResultBuilder;
    }

    public RestResultBuilder timeused(int timeused) {
        this.timeused = timeused;
        return this;
    }

    public RestResultBuilder code(int code) {
        this.code = code;
        return this;
    }

    public RestResultBuilder message(String message) {
        this.message = message;
        return this;
    }

    public RestResultBuilder data(T data) {
        this.data = data;
        return this;
    }

    public RestResultBuilder errorCode(ErrorCode errorCode) {
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getErrorMessage();
        return this;
    }

    public RestResultBuilder success() {
        this.code = GlobalErrorCode.SUCCESS.getErrorCode();
        this.message = GlobalErrorCode.SUCCESS.getErrorMessage();
        return this;
    }

    public RestResultBuilder success(T data) {
        this.code = GlobalErrorCode.SUCCESS.getErrorCode();
        this.message = GlobalErrorCode.SUCCESS.getErrorMessage();
        this.data = data;
        return this;
    }

    public RestResultBuilder failure() {
        this.code = GlobalErrorCode.FAILURE.getErrorCode();
        this.message = GlobalErrorCode.FAILURE.getErrorMessage();
        return this;
    }

    public RestResultBuilder failure(T data) {
        this.code = GlobalErrorCode.FAILURE.getErrorCode();
        this.message = GlobalErrorCode.FAILURE.getErrorMessage();
        this.data = data;
        return this;
    }

    public RestResultBuilder result(boolean successful) {
        if (successful) {
            return this.success();
        } else {
            return this.failure();
        }
    }

    public RestResultBuilder success(Boolean result) {
        if (result == Boolean.TRUE) {
            success();
        } else {
            failure();
        }
        return this;
    }

    public RestResult<T> build() {
        return new RestResult<T>(this.timeused,this.code, this.message, this.data);
    }

    public RestResult build(RestResult restResult) {
        return restResult;
    }
}
