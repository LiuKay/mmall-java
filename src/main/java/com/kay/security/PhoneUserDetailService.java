package com.kay.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface PhoneUserDetailService {
    UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException;
}
