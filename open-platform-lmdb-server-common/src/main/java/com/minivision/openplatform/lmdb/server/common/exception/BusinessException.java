package com.minivision.openplatform.lmdb.server.common.exception;

/**
 * <Description> <br>
 *
 * @author caixing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年07月13日 <br>
 */
public class BusinessException extends RuntimeException {
    protected int code = 500;

    protected ErrorCode errorCode;

    @Deprecated
    public BusinessException(int code) {
        this.code = code;
    }

    //    @Deprecated
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    @Deprecated
    public BusinessException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    @Deprecated
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(formatMsg(errorCode));
        this.code = errorCode.getErrorCode();
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(formatMsg(errorCode), cause);
        this.code = errorCode.getErrorCode();
        this.errorCode = errorCode;
    }

    public int getCode() {
        return this.code;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public final static String formatMsg(ErrorCode errorCode) {
        return String.format("%s-%s", errorCode.getErrorCode(), errorCode.getErrorMessage());
    }
}
