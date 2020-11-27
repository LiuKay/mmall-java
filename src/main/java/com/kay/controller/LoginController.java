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
@Api(value = "登录相关API")
public class LoginController {

    private final AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/smsCode")
    @ApiOperation(value = "获取手机验证码")
    public String createSmsCode(@RequestParam String mobile) throws Exception {
        return authService.getSmsCode(mobile);
    }

    @GetMapping("/login/form")
    @ApiOperation(value = "用户名密码登录")
    public String loginForm(@RequestParam String username, @RequestParam String password) {
        return authService.login(username, password);
    }

    @GetMapping("/login/mobile")
    @ApiOperation(value = "手机验证码登录")
    public String login(@RequestParam String mobile, @RequestParam String smsCode) {
        return authService.loginByMobile(mobile, smsCode);
    }
}
