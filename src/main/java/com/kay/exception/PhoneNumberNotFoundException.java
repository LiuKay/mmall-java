package com.kay.exception;

import com.sun.tools.javac.jvm.Code;

public class PhoneNumberNotFoundException extends CodeRuntimeException {

    private final static String CODE = "e5d63909-90ab-4939-bbc4-7ad2a098cdb6";

    private final static String MSG = "Phone number: %s not found.";

    public PhoneNumberNotFoundException(String phone) {
        super(String.format(MSG, phone), CODE);
    }
}
