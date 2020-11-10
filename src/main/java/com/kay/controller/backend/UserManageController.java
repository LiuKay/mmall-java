package com.kay.controller.backend;

import com.kay.common.Const;
import com.kay.common.ServerResponse;
import com.kay.pojo.User;
import com.kay.service.IUserService;
import com.kay.util.CookieUtil;
import com.kay.util.JsonUtil;
import com.kay.util.RedisShardedPoolUtil;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kay on 2018/3/19.
 * 后台用户管理
 */
@Controller
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    /**
     * 管理员登录
     */
    @PostMapping(value = "/manage/login")
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpServletResponse httpServletResponse,HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole()== Const.ROLE.MANAGE_USER) {
                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2string(user), Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
                return ServerResponse.createBySuccess(user);
            }else {
                //不是管理员用户，无法登录
                return ServerResponse.createByErrorMessage("不是管理员，无法登录");
            }
        }
        return ServerResponse.createByErrorMessage("登录失败");
    }
}
