package com.hampcode.pagoya.customer.exception;

import com.hampcode.pagoya.shared.exception.BusinessRuleException;

public class PhoneAlreadyExistsException extends BusinessRuleException {
    public PhoneAlreadyExistsException() {
        super("el telefono ingresado ya esta registrado");
    }
}
