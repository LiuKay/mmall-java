package com.kay.common;

import com.kay.util.TimestampProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @Value("${application.error.include-trace:false}")
    private boolean includeTrace;

    @Autowired
    private ErrorAttributes errorAttributes;

    @Autowired
    private TimestampProvider timestampProvider;

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public ApiErrorResponse handleError(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Map<String, Object> errorAttributesFromRequest = getErrorAttributes(request, includeTrace);
        String message = (String) errorAttributesFromRequest.get("message");
        String details = (String) errorAttributesFromRequest.get("exception");
        String trace = includeTrace ? (String) errorAttributesFromRequest.get("trace") : null;
        return ApiErrorResponse.builder()
                .status(status)
                .errorCode("No Code Available.")
                .message(message)
                .detail(details)
                .timestamp(timestampProvider.getTimestampAsString())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .trace(trace)
                .build();
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        ErrorAttributeOptions attributeOptions;
        if (includeStackTrace) {
            attributeOptions = ErrorAttributeOptions.of(Include.STACK_TRACE, Include.MESSAGE, Include.EXCEPTION);
        } else {
            attributeOptions = ErrorAttributeOptions.of(Include.MESSAGE, Include.EXCEPTION);
        }

        WebRequest webRequest = new ServletWebRequest(request);
        return errorAttributes.getErrorAttributes(webRequest, attributeOptions);
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
