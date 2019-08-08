package com.jproger.conferencetelegrambot.action.consumers.exceptions;

public class UserActionException extends RuntimeException {
    public UserActionException(String message) {
        super(message);
    }
}
