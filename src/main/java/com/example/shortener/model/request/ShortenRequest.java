package com.example.shortener.model.request;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenRequest {
    private String originalUrl;
}
