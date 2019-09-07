package com.kay.dao;

import com.kay.pojo.User;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String username);

    User selecLogin(@Param("username") String username,@Param("password") String password);

    int checkEmail(String email);

    String selectForgetQuestion(String username);

    int selectQuestionAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);

    int selectOldPassword(@Param("userId")Integer userId, @Param("passwordOld")String passwordOld);

    int checkEmailByUserId(@Param("userId")Integer userId,@Param("email") String email);

    User loadUserByUsername(String username);
}