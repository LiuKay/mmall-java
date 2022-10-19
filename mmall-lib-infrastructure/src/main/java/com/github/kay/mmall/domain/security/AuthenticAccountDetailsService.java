package com.github.kay.mmall.domain.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticAccountDetailsService implements UserDetailsService {

    private final AuthenticAccountRepository accountRepository;

    public AuthenticAccountDetailsService(AuthenticAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return accountRepository.findByUsername(s);
    }
}
