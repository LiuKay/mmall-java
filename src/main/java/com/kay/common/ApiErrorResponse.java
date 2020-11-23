package com.kay.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiErrorResponse {

    private HttpStatus status;

    /* business error code */
    private String errorCode;

    /* international message */
    private String message;

    /* additional message */
    private String detail;

    /* timestamp */
    private String timestamp;

    private String path;

    private String method;
}
