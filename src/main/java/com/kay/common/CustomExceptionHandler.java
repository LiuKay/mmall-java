package com.kay.common;

import com.kay.exception.BaseException;
import com.kay.exception.NotFoundException;
import com.kay.util.TimestampProvider;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.UrlPathHelper;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String NO_CODE_AVAILABLE = "NO_CODE_AVAILABLE";
    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    private final HttpServletRequest httpServletRequest;

    private final TimestampProvider timestampProvider;

    @Autowired
    public CustomExceptionHandler(HttpServletRequest httpServletRequest,
                                  TimestampProvider timestampProvider) {
        this.httpServletRequest = httpServletRequest;
        this.timestampProvider = timestampProvider;
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception) {
        return createErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception exception) {
        return createErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(Exception exception) {
        return createErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ApiErrorResponse> createErrorResponse(Throwable throwable, HttpStatus status) {
        ApiErrorResponse errorResponse = createError(throwable, status);
        return ResponseEntity.status(status)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorResponse);
    }

    private ApiErrorResponse createError(Throwable throwable, HttpStatus status) {
        return ApiErrorResponse.builder()
                               .status(status)
                               .errorCode(getCode(throwable))
                               .message(throwable.getLocalizedMessage())
                               .detail(throwable.getMessage())
                               .timestamp(timestampProvider.getTimestampAsString())
                               .path(URL_PATH_HELPER.getOriginatingServletPath(httpServletRequest))
                               .method(httpServletRequest.getMethod())
                               .build();
    }

    private String getCode(Throwable throwable) {
        if (throwable instanceof BaseException) {
            return ((BaseException) throwable).getCode();
        }
        return NO_CODE_AVAILABLE;
    }

}
