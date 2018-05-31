package com.kay.service.impl;

import com.kay.common.ServerResponse;
import com.kay.pojo.User;
import com.kay.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by kay on 2018/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-config.xml"})
public class UserServiceImplTest {

    @Resource
    private UserServiceImpl userService;

    @Test
    public void resetPassword() {
        User user = new User();
        user.setId(1);
        ServerResponse<String> response = userService.resetPassword("admin", "admin", user);

        System.out.println(JsonUtil.obj2stringPretty(response));
    }
}