package com.kay.service.impl;

import com.kay.dao.UserMapper;
import com.kay.domain.Role;
import com.kay.domain.User;
import com.kay.exception.NotFoundException;
import com.kay.exception.UserAlreadyExistException;
import com.kay.service.UserService;
import com.kay.util.MD5Util;
import com.kay.vo.UserIdentityDTO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * Created by kay on 2018/3/19.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String FORGET_TOKEN_SUFFIX = ":forget_token";


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public UserIdentityDTO register(User user) {
        if (!existedUsername(user.getUsername()) || !existedEmail(user.getEmail())) {
            throw new UserAlreadyExistException("Username or email already existed.");
        }
        user.setPassword(MD5Util.md5EncodeUtf8(user.getPassword()));
        user.setRole(Role.USER);
        userMapper.insert(user);
        return UserIdentityDTO.fromUser(user);
    }


    @Override
    public boolean existedUsername(String username) {
        int userCount = userMapper.checkUserName(username);
        if (userCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean existedEmail(String email) {
        int emailCount = userMapper.checkEmail(email);
        if (emailCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public String forgetGetQuestion(String username) {
        if (!existedUsername(username)) {
            throw new NotFoundException(String.format("Not found username:%s", username));
        }
        return userMapper.selectForgetQuestion(username);
    }

    /**
     * 忘记密码验证回答----token放入redis
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public String checkAnswerAndGenerateToken(String username, String question, String answer) {
        int resultCount = userMapper.selectQuestionAnswer(username, question, answer);
        if (resultCount > 0) {
            String checkToken = UUID.randomUUID().toString();
            redisTemplate.boundValueOps(username + FORGET_TOKEN_SUFFIX).set(checkToken, Duration.ofMinutes(5));
            return checkToken;
        }
        throw new IllegalArgumentException("Safe question answer is incorrect.");
    }

    /**
     * 重置密码：检验token是否有效，重置新密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public void forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (!existedUsername(username)) {
            throw new NotFoundException("Username '%s' not found.");
        }
        //去缓存获取token
        String token = redisTemplate.boundValueOps(username + FORGET_TOKEN_SUFFIX).get();
        if (StringUtils.isBlank(token)) {
            throw new NotFoundException("Forget token is expired.");
        }

        if (!StringUtils.equals(token, forgetToken)) {
            throw new IllegalArgumentException("Forget token is not matched.");
        }
        String md5Password = MD5Util.md5EncodeUtf8(passwordNew);
        userMapper.updatePasswordByUsername(username, md5Password);
    }

    /**
     * 登录用户重置密码
     *
     * @param userId
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @Override
    public void resetPassword(Integer userId, String passwordOld, String passwordNew) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new NotFoundException(String.format("User not found for userId:%s", userId));
        }
        //1.首先校验用户旧密码
        int resultCount = userMapper.selectOldPassword(user.getId(), MD5Util.md5EncodeUtf8(passwordOld));
        if (resultCount == 0) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        //2.设置新密码
        user.setPassword(MD5Util.md5EncodeUtf8(passwordNew));
        userMapper.updateByPrimaryKeySelective(user);
    }


    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @Override
    public void updateUserInfo(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getId(), user.getEmail());
        if (resultCount > 0) {
            throw new IllegalArgumentException("Email has already been used.");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        userMapper.updateByPrimaryKeySelective(updateUser);
    }

    @Override
    public User getUserInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new NotFoundException(String.format("User not found, userId:%s", userId));
        }

        user.setPassword("HIDDEN");
        return user;
    }

}
