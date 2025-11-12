package com.example.shortener.exception;

import com.example.shortener.constant.StatusCode;
import lombok.Getter;

@Getter
public class PageNotFoundException extends RuntimeException{
    private final StatusCode statusCode;
    public PageNotFoundException() {
        super(StatusCode.PAGE_NOT_FOUND.getMessage());
        this.statusCode = StatusCode.PAGE_NOT_FOUND;
    }
}
