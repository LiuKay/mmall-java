package com.kay.controller.portal;

import com.kay.domain.User;
import com.kay.service.AuthService;
import com.kay.service.UserService;
import com.kay.vo.UserIdentityDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("User")
@RestController("/users")
public class UserController {

    private static final String USERNAME = "USERNAME";
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public UserIdentityDTO register(@RequestBody User user) {
        return userService.register(user);
    }

    @ApiOperation("检查用户名或Email")
    @PostMapping("/check_valid/{input}")
    public boolean checkUserName(@PathVariable String input,
                                 @RequestParam(defaultValue = USERNAME) String type) {
        boolean result;
        if (USERNAME.equalsIgnoreCase(input)) {
            result = userService.existedUsername(input);
        } else {
            result = userService.existedEmail(input);
        }
        return result;
    }

    @ApiOperation("忘记密码-获取问题")
    @PostMapping("/forget_get_question")
    public String forgetGetQuestion(@RequestParam String username) {
        return userService.forgetGetQuestion(username);
    }

    @ApiOperation("忘记密码-验证答案")
    @PostMapping("/forget_check_answer")
    public String forgetCheckAnswer(String username, String question, String answer) {
        return userService.checkAnswerAndGenerateToken(username, question, answer);
    }

    @ApiOperation("忘记密码-密码重置")
    @PostMapping("/forget_reset_password")
    public void forgetResetPassword(String username, String passwordNew, String forgetToken) {
        userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @ApiOperation("已登录用户密码重置")
    @PostMapping("{userId}/reset_password")
    public void resetPassword(@PathVariable Integer userId, String passwordOld, String passwordNew,
                              HttpServletRequest request) {
        UserIdentityDTO currentUser = authService.getUser(request);
        if (!currentUser.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cannot change other user's information.");
        }
        userService.resetPassword(userId, passwordOld, passwordNew);
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/update_information")
    public void updateInformation(HttpServletRequest request, User user) {
        UserIdentityDTO currentUser = authService.getUser(request);
        if (!currentUser.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("Cannot change other user's information.");
        }
        user.setUsername(currentUser.getUserName());
        user.setRole(currentUser.getRole());
        userService.updateUserInfo(user);
    }

    @ApiOperation("获取当前用户信息")
    @PostMapping(value = "/get_user_info")
    public User getUserInfo(HttpServletRequest request) {
        return userService.getUserInfo(getUserId(request));
    }

    private Integer getUserId(HttpServletRequest request) {
        return authService.getUserId(request);
    }
}
