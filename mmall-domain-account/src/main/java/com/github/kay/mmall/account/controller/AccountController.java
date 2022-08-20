package com.github.kay.mmall.account.controller;

import com.github.kay.mmall.account.application.AccountApplicationService;
import com.github.kay.mmall.account.domain.validation.AuthenticatedAccount;
import com.github.kay.mmall.account.domain.validation.NotConflictAccount;
import com.github.kay.mmall.account.domain.validation.UniqueAccount;
import com.github.kay.mmall.domain.account.Account;
import com.github.kay.mmall.infrasucture.common.CodedMessage;
import com.github.kay.mmall.infrasucture.common.CommonResponse;
import com.github.kay.mmall.infrasucture.common.ResourceNotFoundException;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //FIXME 此方法的返回包含了 password，需要将 password 和 用户的其他信息分开存储，需要的时候各取所需
    @GetMapping("/{username}")
    @Cacheable(key = "#username")
    @PreAuthorize("#oauth2.hasAnyScope('SERVICE','BROWSER')")
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
    @PreAuthorize("#oauth2.hasAnyScope('BROWSER')")
    public ResponseEntity<CodedMessage> updateUser(
            @RequestBody @AuthenticatedAccount @NotConflictAccount Account user) {
        return CommonResponse.op(() -> service.updateAccount(user));
    }

}
