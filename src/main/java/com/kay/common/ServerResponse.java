package com.kay.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Server Response
 *
 * @deprecated remove
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Deprecated
public class ServerResponse<T> {

    /**
     * 客户端开发人员只允许识别 code
     */
    private int code;

    /**
     * 此 message 只对开发人员所识别，不允许直接暴露给用户，只使用在开发阶段
     */
    private String message;

    /**
     * 成功时返回的数据
     */
    private T data;

    private ServerResponse(int code) {
        this.code = code;
    }

    private ServerResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private ServerResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private ServerResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    public int getCode() {
        return this.code;
    }

    public T getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }

    public static <T> ServerResponse<T> create(ResponseCode status) {
        return new ServerResponse<>(status.getCode(), status.getDescription());
    }

    public static <T> ServerResponse<T> success() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> success(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> successWithMessage(String message) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), message);
    }

    public static <T> ServerResponse<T> successWithData(String message, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> ServerResponse<T> error() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription());
    }

    public static <T> ServerResponse<T> error(String errorMessage) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> error(Throwable throwable) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), ExceptionUtils.getStackTrace(throwable));
    }
}
