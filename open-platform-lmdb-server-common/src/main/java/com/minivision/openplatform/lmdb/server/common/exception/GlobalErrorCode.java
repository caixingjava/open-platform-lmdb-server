package com.minivision.openplatform.lmdb.server.common.exception;

/**
 * <Description> <br>
 *
 * @author caixing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年07月13日 <br>
 */
public enum GlobalErrorCode implements ErrorCode {

    SUCCESS(0000, "Success"),
    FAILURE(1000, "Failure"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    INVALID_PARAM(100, "参数错误");

    private final int errorCode;

    private final String errorMessage;

    GlobalErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 获取错误码
     *
     * @return
     */
    @Override
    public int getErrorCode() {
        return 0;
    }

    /**
     * 获取错误信息
     *
     * @return
     */
    @Override
    public String getErrorMessage() {
        return null;
    }
}
