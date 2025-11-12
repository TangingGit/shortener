package com.example.shortener.controller;

import com.example.shortener.model.request.RegisterRequest;
import com.example.shortener.model.response.CommonResponse;
import com.example.shortener.model.response.authentication.LoginResponse;
import com.example.shortener.service.AuthenticationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public CommonResponse<LoginResponse> login(
            @RequestBody @Validated RegisterRequest loginRequest
    ){
        return new CommonResponse<>(authenticationService.login(loginRequest));
    }

    @PostMapping("/register")
    public CommonResponse<LoginResponse> register(
            @RequestBody @Validated RegisterRequest registerRequest
    ){
        authenticationService.register(registerRequest);
        return new CommonResponse<>();
    }
}
