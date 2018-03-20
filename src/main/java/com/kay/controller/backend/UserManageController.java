package com.kay.controller.backend;

import com.kay.common.Const;
import com.kay.common.ServerResponse;
import com.kay.pojo.User;
import com.kay.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by kay on 2018/3/19.
 * 后台用户管理
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    /**
     * 管理员登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            //管理员才能登录后台
            if (user.getRole().equals(Const.ROLE.MANAGE_USER)) {
                session.setAttribute(Const.CURRENT_USER,user);
            }else {
                //不是管理员用户，无法登录
                return ServerResponse.createByErrorMessage("不是管理远，无法登录");
            }
        }
        return ServerResponse.createByErrorMessage("登录失败");
    }
}
