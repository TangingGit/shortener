package com.example.shortener.model.response.shorten;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenResponse {
    private String originalUrl;
    private String shortUrl;
}
