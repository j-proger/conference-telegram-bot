package com.jproger.conferencetelegrambot.common.operations.exceptions;

public class UserActionException extends RuntimeException {
    public UserActionException(String message) {
        super(message);
    }
}
