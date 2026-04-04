package com.password.exception;

import lombok.Getter;

@Getter
public class PasswordException extends RuntimeException {

    private final String userMessage;

    public PasswordException(String logMessage, String userMessage) {
        super(logMessage);
        this.userMessage = userMessage;
    }
}
