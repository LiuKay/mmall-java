package com.github.kay.mmall.domain.auth;

import com.github.kay.mmall.domain.account.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class AuthenticAccount extends Account implements UserDetails {

    private Collection<GrantedAuthority> authorities = new HashSet<>();

    public AuthenticAccount() {
        super();
        authorities.add(new SimpleGrantedAuthority(Role.USER));
    }

    public AuthenticAccount(Account origin) {
        this();
        BeanUtils.copyProperties(origin, this);
        if (getId() == 1) {
            // 由于没有做用户管理功能，默认给系统中第一个用户赋予管理员角色
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
