package com.kay.security;

import com.kay.dao.UserMapper;
import com.kay.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kay
 * @date 2019/9/6 22:54
 */
@Component
@Slf4j
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("username:" + username + " not found.");
        }
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,user"));
    }
}
