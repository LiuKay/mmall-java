package com.kay.security;

import com.kay.dao.UserMapper;
import com.kay.domain.User;
import com.kay.exception.PhoneNumberNotFoundException;
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
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Username: %s not found.", username));
        }
        return parseToUserDetails(user);
    }

    @Override
    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        User user = userMapper.loadUserByPhone(phone);
        if (user == null) {
            throw new PhoneNumberNotFoundException(phone);
        }
        return parseToUserDetails(user);
    }

    private UserDetails parseToUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .disabled(false)
                .credentialsExpired(false)
                .build();
    }
}
