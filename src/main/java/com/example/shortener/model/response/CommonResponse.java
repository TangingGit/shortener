package com.example.shortener.model.response;

import com.example.shortener.constant.StatusCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T> {
    private String responseCode;
    private String responseMessage;
    private T data;

    public CommonResponse(T data){
        this.responseCode = StatusCode.SUCCESS.getCode();
        this.responseMessage = StatusCode.SUCCESS.getMessage();
        this.data = data;
    }
    public CommonResponse(){
        this.responseCode = StatusCode.SUCCESS.getCode();
        this.responseMessage = StatusCode.SUCCESS.getMessage();
        this.data = null;
    }

    public CommonResponse(StatusCode statusCode){
        this.responseCode = statusCode.getCode();
        this.responseMessage = statusCode.getMessage();
        this.data = null;
    }
}
