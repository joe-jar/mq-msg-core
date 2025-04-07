package com.example.msgibm.exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(Long id) {
        super("Message not found with ID: " + id);
    }
}
