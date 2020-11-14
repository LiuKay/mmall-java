package com.kay.security;

import org.springframework.security.core.AuthenticationException;

public class PhoneNumberNotFoundException extends AuthenticationException {

    private final static String MSG = "Phone number: %s not found.";

    public PhoneNumberNotFoundException(String phone) {
        super(String.format(MSG, phone));
    }
}
