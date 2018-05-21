package com.kay.service.impl;

import com.kay.common.Const;
import com.kay.common.ServerResponse;
import com.kay.dao.UserMapper;
import com.kay.pojo.User;
import com.kay.service.IUserService;
import com.kay.util.MD5Util;
import com.kay.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by kay on 2018/3/19.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username,String password) {
        int userCount = userMapper.checkUserName(username);
        if (userCount==0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selecLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        //登录成功，敏感信息不返回,可以新建一个专门返回的 UserVo
        user.setPassword(StringUtils.EMPTY);
        user.setQuestion(StringUtils.EMPTY);
        user.setAnswer(StringUtils.EMPTY);
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
            return ServerResponse.createByErrorMessage("注册失败");
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
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int emailCount = userMapper.checkEmail(str);
                if (emailCount > 0) {
                    return ServerResponse.createByErrorMessage("email已被注册");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        ServerResponse<String> valid = this.checkValid(username, Const.USERNAME);
        if(valid.isSuccess()){
            //成功说明checkValid检验重复的用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        //查找忘记密码问题
        String question=userMapper.selectForgetQuestion(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErrorMessage("提示问题的答案为空");
    }

    /**
     * 忘记密码验证回答----token放入redis
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
            RedisPoolUtil.setEx(Const.TOKEN_PREFIX + username, checkToken,60*60*12);
            return ServerResponse.createBySuccess(checkToken); //返回token
        }

        return ServerResponse.createByErrorMessage("验证失败");
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
            return ServerResponse.createByErrorMessage("token不能为空");
        }
        //2.检验用户的合法性，否则直接获取token前缀的key去拿token的value会有风险
        ServerResponse<String> valid = this.checkValid(username, Const.USERNAME);
        if(valid.isSuccess()){
            //成功说明checkValid检验重复的用户不存在
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //去缓存获取token
        String token = RedisPoolUtil.get(Const.TOKEN_PREFIX);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或已过期，请重新获取");
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
            return ServerResponse.createByErrorMessage("token错误，请重新获取token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 登录用户重置密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //1.首先校验用户旧密码
        int resultCount = userMapper.selectOldPassword(user.getId(), MD5Util.MD5EncodeUtf8(passwordOld));
        if (resultCount==0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        //2.设置新密码
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码重置成功");
        }
        return ServerResponse.createByErrorMessage("密码重置失败");
    }



    /**
     *  更新用户信息
     * @param user
     * @return
     */
    @Override
    public ServerResponse updateUserInfo(User user) {
        //1.检验Email的合法性，不能是别人已经注册的email
        int resultCount = userMapper.checkEmailByUserId(user.getId(), user.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已经存在，请更换");
        }
        //2.更新用户信息----注意，用户名不能被更新
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        //fixme 有个字段upadateTime 默认设置为mysql的now() 函数，但是是mybatis的动态sql判断upadateTime不为空时更新，应该改为只要更新了信息就更新该字段
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount>0) {
            return ServerResponse.createBySuccessMessage("用户信息更新成功");
        }
        return ServerResponse.createByErrorMessage("用户信息更新失败");
    }

    /**
     * 获取用户信息---注意用户密码不能返回
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<User> getUserInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("未找到用户");
        }
        //注意：用户密码不能返回
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }



    /**
     * 检查是否为管理员
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user!= null && user.getRole().intValue() == Const.ROLE.MANAGE_USER) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


}
