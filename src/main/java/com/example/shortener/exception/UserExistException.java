package com.example.shortener.exception;

import com.example.shortener.constant.StatusCode;
import lombok.Getter;

@Getter
public class UserExistException extends RuntimeException{
    private final StatusCode statusCode;
    public UserExistException() {
        super(StatusCode.USER_EXIST.getMessage());
        this.statusCode = StatusCode.USER_EXIST;
    }
}
