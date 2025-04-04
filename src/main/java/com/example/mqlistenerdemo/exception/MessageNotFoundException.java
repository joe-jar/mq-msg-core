package com.example.mqlistenerdemo.exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(Long id) {
        super("Message not found with ID: " + id);
    }
}
