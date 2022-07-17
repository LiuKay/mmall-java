package com.github.kay.mmall.resource;

import com.github.kay.mmall.application.AccountApplicationService;
import com.github.kay.mmall.domain.account.Account;
import com.github.kay.mmall.domain.account.validation.AuthenticatedAccount;
import com.github.kay.mmall.domain.account.validation.NotConflictAccount;
import com.github.kay.mmall.domain.account.validation.UniqueAccount;
import com.github.kay.mmall.infrasucture.common.CodedMessage;
import com.github.kay.mmall.infrasucture.common.CommonResponse;
import com.github.kay.mmall.infrasucture.common.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Validated
@CacheConfig(cacheNames = "resource.account")
@RestController
@RequestMapping("/restful/accounts")
public class AccountController {

    private final AccountApplicationService service;

    public AccountController(AccountApplicationService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    @Cacheable(key = "#username")
    public Account getUser(@PathVariable String username) {
        return Optional.ofNullable(service.findAccountByUsername(username))
                       .orElseThrow(
                               ResourceNotFoundException.supplier(String.format("Not found for username:%s", username)));
    }

    @PostMapping
    @CacheEvict(key = "#user.username")
    public ResponseEntity<CodedMessage> createUser(@RequestBody @UniqueAccount Account user) {
        return CommonResponse.op(() -> service.createAccount(user));
    }

    @PutMapping
    @CacheEvict(key = "#user.username")
    public ResponseEntity<CodedMessage> updateUser(
            @RequestBody @AuthenticatedAccount @NotConflictAccount Account user) {
        return CommonResponse.op(() -> service.updateAccount(user));
    }

}
