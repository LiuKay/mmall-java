package com.kay.service;

import com.kay.common.ServerResponse;
import com.kay.domain.User;

import lombok.NonNull;

/**
 * Created by kay on 2018/3/19.
 */
public interface UserService {

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, @NonNull RegisterType registerType);

    ServerResponse<String> forgetGetQuestion(String username);

    ServerResponse<String> checkQuestionAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse updateUserInfo(User user);

    ServerResponse<User> getUserInfo(Integer userId);

    ServerResponse checkAdminRole(User user);
}
