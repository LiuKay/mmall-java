package com.kay.controller.portal;

import com.kay.common.Const;
import com.kay.common.ServerResponse;
import com.kay.domain.User;
import com.kay.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by kay on 2018/5/21. Spring Session Test
 */
@RestController
@RequestMapping("/user/springsessionTest")
public class UserSpringSessionController {

    @Autowired
    private UserService userService;

    /**
     * 登出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session, HttpServletRequest request,
                                         HttpServletResponse response) {
        /*String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        RedisShardedPoolUtil.del(loginToken);*/
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.success();
    }


    /**
     * 获取当前用户信息
     *
     * @return
     */
    @RequestMapping(value = "/get_user_info", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        /*String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);*/

        if (user != null) {
            return ServerResponse.success(user);
        }
        return ServerResponse.error("用户未登录,无法获取当前用户的信息");
    }

}
