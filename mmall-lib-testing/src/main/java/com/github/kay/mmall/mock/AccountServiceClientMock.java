package com.github.kay.mmall.mock;

import com.github.kay.mmall.domain.account.Account;
import com.github.kay.mmall.domain.security.AccountServiceClient;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceClientMock implements AccountServiceClient {

    @Override
    public Account findByUsername(String username) {
        if (username.equals("kaybee")) {
            Account account = new Account();
            account.setId(1);
            account.setUsername("kaybee");
            account.setPassword("$2a$10$SvyLUrNMbwPAnPBvDBbItOtLnmg88IbKFnBQy4zjaRNCCi0p5OCya");
            account.setName("kaybee");
            account.setTelephone("18888888888");
            account.setEmail("kaybee@gmail.com");
            account.setLocation("北京市");
            return account;
        } else {
            return null;
        }
    }
}
