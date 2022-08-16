package com.github.kay.mmall.controller;

import com.github.kay.mmall.application.AddressApplicationService;
import com.github.kay.mmall.domain.account.Address;
import com.github.kay.mmall.domain.auth.AuthenticAccount;
import com.github.kay.mmall.infrasucture.common.CodedMessage;
import com.github.kay.mmall.infrasucture.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/restful/addresses")
public class AddressController {

    private final AddressApplicationService service;

    public AddressController(AddressApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<Address> listAddresses(){
        AuthenticAccount account = getCurrentUser();
        return service.listAddresses(account.getId());
    }

    private AuthenticAccount getCurrentUser() {
        final Object principal = SecurityContextHolder.getContext()
                                                      .getAuthentication()
                                                      .getPrincipal();
        return (AuthenticAccount) principal;
    }

    @PostMapping
    public ResponseEntity<CodedMessage> createAddress(@RequestBody Address address){
        address.setUserId(getCurrentUser().getId());
        return CommonResponse.op(() -> service.createAddress(address));
    }
}
