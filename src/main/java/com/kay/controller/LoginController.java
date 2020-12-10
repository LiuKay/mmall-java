package com.kay.controller;

import static com.kay.security.properties.SecurityConstants.ORIGINAL_REQUEST_METHOD;
import static com.kay.security.properties.SecurityConstants.REQUEST_PARAMETER_MOBILE;
import static com.kay.security.properties.SecurityConstants.REQUEST_PARAMETER_SMS_CODE;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

import com.kay.common.ApiErrorResponse;
import com.kay.security.properties.SecurityConstants;
import com.kay.service.AuthService;
import com.kay.util.DateTimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiuKay
 * @since 2019/12/5
 */
@RestController
@Api(value = "验证相关API")
public class LoginController {

    private final AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(SecurityConstants.VERIFICATION_CODE_URL)
    @ApiOperation(value = "获取手机验证码")
    public void sendSmsCode(@RequestParam String mobile) {
        authService.sendVerificationCode(mobile);
    }


    /**
     * this will be handle by security filter
     */
    @PostMapping(SecurityConstants.LOGIN_FORM_PROCESSING_URL)
    @ApiOperation(value = "login form")
    public void loginForm(@RequestParam(SPRING_SECURITY_FORM_USERNAME_KEY) String username,
                                    @RequestParam(SPRING_SECURITY_FORM_PASSWORD_KEY) String password) {
    }

    /**
     * this will be handle by security filter
     */
    @PostMapping(SecurityConstants.LOGIN_MOBILE_PROCESSING_URL)
    @ApiOperation(value = "login mobile")
    public void loginMobile(@RequestParam(REQUEST_PARAMETER_MOBILE) String mobile,
                                      @RequestParam(REQUEST_PARAMETER_SMS_CODE) String smsCode) {
    }


    @ApiOperation(value = "miss token",hidden = true)
    @GetMapping(SecurityConstants.LOGIN_REQUIRE)
    public ApiErrorResponse requireLogin(HttpServletRequest request) {
        return ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorCode(HttpStatus.UNAUTHORIZED.toString())
                .message("Need Login.")
                .detail("Require Authentication, Please Login First.")
                .timestamp(DateTimeUtils.getTimestampAsString())
                .path((String) request.getAttribute(ORIGINAL_REQUEST_METHOD))
                .build();
    }
}
