package com.kay.security;

import com.kay.dao.UserMapper;
import com.kay.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class MmallUserDetailService implements UserDetailsService, PhoneUserDetailService {

    private final UserMapper userMapper;

    public MmallUserDetailService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        //TODO: hide password
        return user;
    }

    @Override
    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        User user = userMapper.loadUserByPhone(phone);
        //TODO: hide password
        return user;
    }
}
