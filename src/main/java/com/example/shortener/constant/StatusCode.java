package com.example.shortener.constant;

import lombok.Getter;

@Getter
public enum StatusCode {
    SUCCESS("200-000","success");

    private final String code;
    private final String message;

    StatusCode(String code, String message){
        this.code = code;
        this.message = message;
    }
}
