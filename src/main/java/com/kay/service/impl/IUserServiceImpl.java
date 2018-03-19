package com.kay.service.impl;

import com.kay.common.Const;
import com.kay.common.ServerResponse;
import com.kay.common.TokenCache;
import com.kay.dao.UserMapper;
import com.kay.pojo.User;
import com.kay.service.IUserService;
import com.kay.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by kay on 2018/3/19.
 */
@Service("iUserService")
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username,String password) {
        int userCount = userMapper.checkUserName(username);
        if (userCount==0) {
            return ServerResponse.createByErorrMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selecLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErorrMessage("密码错误");
        }

        //登录成功，但是不能返回密码
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        //验证用户名和email
        ServerResponse<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //密码加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setRole(Const.ROLE.NORMAL_USER);
        int userCount =userMapper.insert(user);

        if (userCount == 0) {
            return ServerResponse.createByErorrMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }


    /**
     * 验证用户名和密码
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int userCount = userMapper.checkUserName(str);
                if (userCount > 0) {
                    return ServerResponse.createByErorrMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int emailCount = userMapper.checkEmail(str);
                if (emailCount > 0) {
                    return ServerResponse.createByErorrMessage("email已被注册");
                }
            }
        }else {
            return ServerResponse.createByErorrMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        ServerResponse<String> valid = this.checkValid(username, Const.USERNAME);
        if(valid.isSuccess()){
            //成功说明checkValid检验重复的用户不存在
            return ServerResponse.createByErorrMessage("用户不存在");
        }

        //查找忘记密码问题
        String question=userMapper.selectForgetQuestion(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErorrMessage("提示问题的答案为空");
    }

    /**
     * 忘记密码验证回答----使用本地缓存
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkQuestionAnswer(String username, String question, String answer) {
        int resultCount = userMapper.selectQuestionAnswer(username, question, answer);
        //有此用户即对应的问题也回答正确
        if (resultCount > 0) {
            String checkToken = UUID.randomUUID().toString();  //生成随机的token字符串
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username,checkToken); //将token放入本地缓存
            return ServerResponse.createBySuccess(checkToken); //返回token
        }

        return ServerResponse.createByErorrMessage("验证失败");
    }

    /**
     * 重置密码：检验token是否有效，重置新密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        //1.检验传入的token是否为空，为空直接返回
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErorrMessage("token不能为空");
        }
        //2.检验用户的合法性，否则直接获取token前缀的key去拿token的value会有风险
        ServerResponse<String> valid = this.checkValid(username, Const.USERNAME);
        if(valid.isSuccess()){
            //成功说明checkValid检验重复的用户不存在
            return ServerResponse.createByErorrMessage("用户名不存在");
        }
        //去缓存获取token
        String token = TokenCache.getValue(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErorrMessage("token无效或已过期，请重新获取");
        }

        if (StringUtils.equals(token,forgetToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            //更新新密码
            int updateCount = userMapper.updatePasswordByUsername(username,md5Password);
            if (updateCount > 0) {
                //判断更新行数
                return ServerResponse.createBySuccessMessage("密码重置成功");
            }
        }else {
            return ServerResponse.createByErorrMessage("token错误，请重新获取token");
        }
        return ServerResponse.createByErorrMessage("修改密码失败");
    }
}
