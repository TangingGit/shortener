package com.example.shortener.controller;

import com.example.shortener.model.request.ShortenRequest;
import com.example.shortener.model.response.CommonResponse;
import com.example.shortener.model.response.shorten.ShortenResponse;
import com.example.shortener.repository.entity.ShortenUrlEntity;
import com.example.shortener.service.ShortenService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShortenController {
    private final ShortenService shortenService;
    ShortenController(ShortenService shortenService){
        this.shortenService = shortenService;
    }
    @PostMapping("/shorten")
    public CommonResponse<ShortenResponse> shorten(
            @RequestBody @Validated ShortenRequest shortenRequest
    ){

        return new CommonResponse<>(shortenService.shortenUrl(shortenRequest));
    }

    @GetMapping("/urls")
    public CommonResponse<List<ShortenUrlEntity>> getAllUrls(
            @RequestHeader("Authorization") String token
    ){
        return new CommonResponse<>(shortenService.getAllUrls(token));
    }
}
