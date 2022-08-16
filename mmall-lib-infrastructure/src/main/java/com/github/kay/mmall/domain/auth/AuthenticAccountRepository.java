package com.github.kay.mmall.domain.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticAccountRepository {

    @Autowired
    private AccountServiceClient accountServiceClient;

    public AuthenticAccount findByUsername(String username) {
        return new AuthenticAccount(
                Optional.ofNullable(accountServiceClient.findByUsername(username))
                        .orElseThrow(
                                () -> new UsernameNotFoundException(String.format("User '%s' is not existed.", username)))
        );
    }
}
