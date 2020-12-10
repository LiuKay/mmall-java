package com.kay.common;

import com.kay.config.AppConfigProperties;
import com.kay.util.DateTimeUtils;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @Autowired
    private AppConfigProperties properties;

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public ApiErrorResponse handleError(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Map<String, Object> errorAttributesFromRequest = getErrorAttributes(request, properties.isIncludeErrorTrace());
        String message = (String) errorAttributesFromRequest.get("message");
        String details = (String) errorAttributesFromRequest.get("exception");
        String trace = properties.isIncludeErrorTrace() ? (String) errorAttributesFromRequest.get("trace") : null;
        return ApiErrorResponse.builder()
                .status(status)
                .errorCode("No Code Available.")
                .message(message)
                .detail(details)
                .timestamp(DateTimeUtils.getTimestampAsString())
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
