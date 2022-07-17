package com.github.kay.mmall.domain.auth;

import com.github.kay.mmall.domain.account.AccountRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticAccountRepository {

    private final AccountRepository accountRepository;

    public AuthenticAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public AuthenticAccount findByUsername(String username) {
        return new AuthenticAccount(
                Optional.ofNullable(accountRepository.findByUsername(username))
                        .orElseThrow(
                                () -> new UsernameNotFoundException(String.format("User '%s' is not existed.", username)))
        );
    }
}
