package com.kay.controller.portal;

import com.kay.common.ServerResponse;
import com.kay.domain.User;
import com.kay.service.AuthService;
import com.kay.service.UserService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/19.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }

    /**
     * 验证用户名和email是否已经存在
     * @param str
     * @param type
     * @return
     */
    @PostMapping("/check_valid")
    public ServerResponse<String> checkUserName(String str,String type){
        return userService.checkValid(str, type);
    }

    /**
     * 忘记密码获取问题
     * @param username
     * @return
     */
    @PostMapping("/forget_get_question")
    public ServerResponse<String> forgetGetQuestion(String username) {
        return userService.forgetGetQuestion(username);
    }

    /**
     * 忘记密码验证回答
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @PostMapping("/forget_check_answer")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return userService.checkQuestionAnswer(username, question, answer);
    }

    /**
     *验证通过后重置密码，带着上次返回的token
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping("/forget_reset_password")
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 已登录用户的重置密码
     * @param passwordOld
     * @param passwordNew
     * @param request
     * @return
     */
    //TODO: refactor
    @PostMapping("/reset_password")
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, HttpServletRequest request) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
//
//        if (user == null) {
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        return userService.resetPassword(passwordOld, passwordNew, user);
        return null;
    }

    /**
     * 更新用户信息-----注意横向越权问题
     * @param request
     * @param user
     * @return
     */
    //TODO: refactor
    @PostMapping("/update_information")
    public ServerResponse updateInformation(HttpServletRequest request, User user) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        User currentUser = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
//
//        //此处用户id前端可能回传过来，也有可能不传，所以设置一下保险
//        //同时此处也为了防止横向越权的问题，即传过来的id并不是当前登录的用户id
//        user.setId(currentUser.getId());
//        user.setUsername(currentUser.getUsername()); //username 不能被更新
//        ServerResponse response = userService.updateUserInfo(user);
//        if (response.isSuccess()) {
////            session.setAttribute(Const.CURRENT_USER,response.getData());
//            //更新Redis中用户信息
//            RedisShardedPoolUtil.setEx(loginToken, JsonUtil.obj2string(response.getData()), Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
//            return ServerResponse.createBySuccessMessage("更新成功");
//        }
//        return response;
        return null;
    }

    /**
     * 获取当前用户信息
     * @return
     */
    //TODO: refactor
    @PostMapping(value = "/get_user_info")
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
//        }
//        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if(user != null){
//            return ServerResponse.createBySuccess(user);
//        }
        return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
    }

    /**
     * 获取当前用户详细信息
     * @param request
     * @return
     */
    //TODO: refactor
    @PostMapping(value = "/get_information")
    public ServerResponse<User> get_information(HttpServletRequest request) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
//        }
//        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
//        if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
//        }
//        return userService.getUserInfo(user.getId());
        return null;
    }

    private Integer getUserId(HttpServletRequest request) {
        return authService.getUserId(request);
    }
}
