package com.example.shortener.exception;

import com.example.shortener.constant.StatusCode;
import lombok.Getter;

@Getter
public class JwtInvalidException extends RuntimeException{
    private final StatusCode statusCode;
    public JwtInvalidException() {
        super(StatusCode.JWT_INVALID.getMessage());
        this.statusCode = StatusCode.JWT_INVALID;
    }
}
