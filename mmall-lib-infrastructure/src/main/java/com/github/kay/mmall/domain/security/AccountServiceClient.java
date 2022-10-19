package com.github.kay.mmall.domain.security;

import com.github.kay.mmall.domain.account.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account")
public interface AccountServiceClient {

    @GetMapping("/restful/accounts/{username}")
    Account findByUsername(@PathVariable("username") String username);
}
