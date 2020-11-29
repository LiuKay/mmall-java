package com.kay.controller.portal;

import com.kay.domain.User;
import com.kay.service.AuthService;
import com.kay.service.UserService;
import com.kay.vo.UserIdentityDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public UserIdentityDTO register(User user) {
        return userService.register(user);
    }

    /**
     * 验证用户名和email是否已经存在
     *
     * @param str
     * @param type
     * @return
     */
    @PostMapping("/check_valid")
    public boolean checkUserName(String input, String type) {
        boolean result;
        if ("username".equalsIgnoreCase(input)) {
            result = userService.existedUsername(input);
        } else {
            result = userService.existedEmail(input);
        }
        return result;
    }

    /**
     * 忘记密码获取问题
     *
     * @param username
     * @return
     */
    @PostMapping("/forget_get_question")
    public String forgetGetQuestion(String username) {
        return userService.forgetGetQuestion(username);
    }

    /**
     * 忘记密码验证回答
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @PostMapping("/forget_check_answer")
    public String forgetCheckAnswer(String username, String question, String answer) {
        return userService.checkAnswerAndGenerateToken(username, question, answer);
    }

    /**
     * 验证通过后重置密码，带着上次返回的token
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping("/forget_reset_password")
    public void forgetResetPassword(String username, String passwordNew, String forgetToken) {
        userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @PostMapping("{userId}/reset_password")
    public void resetPassword(@PathVariable Integer userId, String passwordOld, String passwordNew,
                              HttpServletRequest request) {
        UserIdentityDTO currentUser = authService.getUser(request);
        if (!currentUser.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cannot change other user's infomation.");
        }
        userService.resetPassword(userId, passwordOld, passwordNew);
    }

    /**
     * 更新用户信息-----注意横向越权问题
     *
     * @param request
     * @param user
     * @return
     */
    @PostMapping("/update_information")
    public void updateInformation(HttpServletRequest request, User user) {
        UserIdentityDTO currentUser = authService.getUser(request);
        if (!currentUser.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("Cannot change other user's infomation.");
        }
        //同时此处也为了防止横向越权的问题，即传过来的id并不是当前登录的用户id
        user.setUsername(currentUser.getUserName());
        user.setRole(currentUser.getRole());
        userService.updateUserInfo(user);
    }

    @PostMapping(value = "/get_user_info")
    public User getUserInfo(HttpServletRequest request) {
        return userService.getUserInfo(getUserId(request));
    }


    private Integer getUserId(HttpServletRequest request) {
        return authService.getUserId(request);
    }
}
