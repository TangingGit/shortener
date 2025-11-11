package com.example.shortener.controller;

import com.example.shortener.model.request.ShortenRequest;
import com.example.shortener.model.response.shorten.ShortenResponse;
import com.example.shortener.service.ShortenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShortenController {
    private final ShortenService shortenService;
    ShortenController(ShortenService shortenService){
        this.shortenService = shortenService;
    }
    @PostMapping("/shorten")
    public ShortenResponse shorten(
            @RequestBody ShortenRequest shortenRequest
    ){

        return shortenService.shortenUrl(shortenRequest);
    }
}
