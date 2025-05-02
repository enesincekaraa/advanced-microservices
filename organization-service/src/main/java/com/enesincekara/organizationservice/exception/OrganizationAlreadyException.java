package com.enesincekara.organizationservice.exception;

public class OrganizationAlreadyException extends RuntimeException {
    public OrganizationAlreadyException(String message) {
        super(message);
    }
}
