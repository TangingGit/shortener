package com.example.shortener.exception;

import com.example.shortener.constant.StatusCode;
import com.example.shortener.model.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdentifyException.class)
    public ResponseEntity handleIdentifyException(IdentifyException ex){
        log.error("handleIdentifyException error: ", ex);
        return ResponseEntity
                .status(403)
                .body(new CommonResponse(ex.getStatusCode()));
    }

    @ExceptionHandler(JwtInvalidException.class)
    public ResponseEntity handleJwtInvalidException(JwtInvalidException ex){
        log.error("JwtInvalidException error: ", ex);
        return ResponseEntity
                .status(403)
                .body(new CommonResponse(ex.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex){
        log.error("handleException error: ", ex);
        return ResponseEntity
                .status(500)
                .body(new CommonResponse(StatusCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity handleUserExistException(UserExistException ex){
        log.error("JwtInvalidException error: ", ex);
        return ResponseEntity
                .status(400)
                .body(new CommonResponse(ex.getStatusCode()));
    }

}
