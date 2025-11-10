package com.example.shortener.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T> {
    private String responseCode;
    private String responseMessage;
    private T data;

    public CommonResponse(T data){
        this.responseCode = "200-000";
        this.responseMessage = "Success";
        this.data = data;
    }
    public CommonResponse(){
        this.responseCode = "200-000";
        this.responseMessage = "Success";
        this.data = null;
    }
}
