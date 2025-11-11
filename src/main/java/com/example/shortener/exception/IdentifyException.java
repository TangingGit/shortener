package com.example.shortener.exception;

import com.example.shortener.constant.StatusCode;
import lombok.Getter;

@Getter
public class IdentifyException extends RuntimeException{
    private final StatusCode statusCode;
    public IdentifyException() {
        super(StatusCode.USER_OR_PASSWORD_INVALID.getMessage());
        this.statusCode = StatusCode.USER_OR_PASSWORD_INVALID;
    }
}
