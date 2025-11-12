package com.example.shortener.constant;

import lombok.Getter;

@Getter
public enum StatusCode {
    SUCCESS("200-000","success"),
    //400
    USER_EXIST("400-001","user is exist"),
    INVALID_FORMAT("400-002","invalid format"),
    //404
    PAGE_NOT_FOUND("404-001","page not found"),
    //403
    USER_OR_PASSWORD_INVALID("403-001","user or password invalid"),
    JWT_INVALID("403-002","jwt invalid"),
    //500
    INTERNAL_SERVER_ERROR("500-001","internal server error");
    private final String code;
    private final String message;

    StatusCode(String code, String message){
        this.code = code;
        this.message = message;
    }
}
