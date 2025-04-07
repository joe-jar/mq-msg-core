package com.example.msgibm.exception;

public class PartnerNotFoundException extends RuntimeException {
    public PartnerNotFoundException(Long id) {
        super("Partner not found with ID: " + id);
    }
}
