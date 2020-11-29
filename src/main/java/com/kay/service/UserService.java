package com.kay.service;

import com.kay.domain.User;
import com.kay.vo.UserIdentityDTO;

/**
 * Created by kay on 2018/3/19.
 */
public interface UserService {

    UserIdentityDTO register(User user);

    boolean existedUsername(String username);

    boolean existedEmail(String email);

    String forgetGetQuestion(String username);

    String checkAnswerAndGenerateToken(String username, String question, String answer);

    void forgetResetPassword(String username, String passwordNew, String forgetToken);

    void resetPassword(Integer userId, String passwordOld, String passwordNew);

    void updateUserInfo(User user);

    User getUserInfo(Integer userId);

}
