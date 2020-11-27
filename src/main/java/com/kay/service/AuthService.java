package com.kay.service;

import com.kay.vo.UserIdentityDTO;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    UserIdentityDTO getUser(HttpServletRequest request);

    Integer getUserId(HttpServletRequest request);

    String getSmsCode(String mobile);

    String login(String username, String password);

    String loginByMobile(String mobile, String smsCode);
}
