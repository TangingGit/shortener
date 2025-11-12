package com.example.shortener.model.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
public class RegisterRequest {
    @Email
    private String email;
    private String password;
}
