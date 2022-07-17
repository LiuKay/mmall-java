package com.github.kay.mmall.application;

import com.github.kay.mmall.domain.account.Account;
import com.github.kay.mmall.domain.account.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountApplicationService {

    private final AccountRepository repository;
    private final PasswordEncoder encoder;

    public AccountApplicationService(AccountRepository repository,
                                     PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void createAccount(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        repository.save(account);
    }

    public Account findAccountByUsername(String username) {
        return repository.findByUsername(username);
    }

    public void updateAccount(Account account) {
        repository.save(account);
    }

}
