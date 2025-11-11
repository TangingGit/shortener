package com.example.shortener.controller;

import com.example.shortener.service.ShortenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping
public class RedirectController {
    private final ShortenService shortenService;
    RedirectController(ShortenService shortenService){
        this.shortenService = shortenService;
    }

    @GetMapping("/r/{shortenUrl}")
    public RedirectView redirect(
            @PathVariable String shortenUrl
    ){
        String url = shortenService.getRedirectUrl(shortenUrl);
        return new RedirectView(url);
    }
}
