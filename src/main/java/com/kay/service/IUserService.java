package com.kay.service;

import com.kay.common.ServerResponse;
import com.kay.pojo.User;

/**
 * Created by kay on 2018/3/19.
 */
public interface IUserService {

    ServerResponse<User> login(String username,String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> forgetGetQuestion(String username);

    ServerResponse<String> checkQuestionAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse updateUserInfo(User user);

    ServerResponse<User> getUserInfo(Integer userId);

    ServerResponse checkAdminRole(User user);
}
