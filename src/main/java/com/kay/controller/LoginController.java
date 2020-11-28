package com.kay.controller;

import com.kay.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

    @GetMapping("/code/smsCode")
    @ApiOperation(value = "获取手机验证码")
    public void createSmsCode(@RequestParam String mobile) {
        authService.sendVerificationCode(mobile);
    }

}
