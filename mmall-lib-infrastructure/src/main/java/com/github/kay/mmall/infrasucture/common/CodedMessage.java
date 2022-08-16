package com.github.kay.mmall.infrasucture.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 目前大多数前端用到的结构，习惯使用 JSON-RPC 的这种格式，而不是以 HTTP 状态码为准
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CodedMessage {
    public static final Integer CODE_SUCCESS = 0;
    public static final Integer CODE_DEFAULT_FAILURE = 1;

    private Integer code;
    private String message;
    private Object data;

    public CodedMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
