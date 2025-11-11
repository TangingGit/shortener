package com.example.shortener.exception;

import com.example.shortener.constant.StatusCode;
import com.example.shortener.model.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdentifyException.class)
    public ResponseEntity handleIdentifyException(IdentifyException ex){
        return ResponseEntity
                .status(403)
                .body(new CommonResponse(ex.getStatusCode()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity handleIdentifyException(Exception ex){
        return ResponseEntity
                .status(500)
                .body(new CommonResponse(StatusCode.INTERNAL_SERVER_ERROR));
    }
}
