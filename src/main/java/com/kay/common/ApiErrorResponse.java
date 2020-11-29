package com.kay.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private Integer status;

    /* business error code for developer*/
    private String errorCode;

    /* international message */
    private String message;

    /* additional message */
    private String detail;

    /* timestamp */
    private String timestamp;

    private String path;

    private String method;

    public String trace;
}
