package com.example.shortener.controller;

import com.example.shortener.model.response.CommonResponse;
import com.example.shortener.model.response.authentication.LoginResponse;
import com.example.shortener.service.AuthenticationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public CommonResponse<LoginResponse> login(){
        return new CommonResponse<>(authenticationService.login());
    }
}
