package com.example.shortener.service;

import com.example.shortener.model.request.ShortenRequest;
import com.example.shortener.model.response.shorten.ShortenResponse;
import com.example.shortener.repository.ShortenUrlRepository;
import com.example.shortener.repository.entity.ShortenUrlEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ShortenService {
    private static final String allowedString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final char[] allowedCharacters = allowedString.toCharArray();
    private final int base = allowedCharacters.length;
    private final ShortenUrlRepository shortenUrlRepository;
    private final String shortenBaseUrl;
    ShortenService(ShortenUrlRepository shortenUrlRepository, @Value("${app.shorten.base-url}") String shortenBaseUrl){
        this.shortenUrlRepository = shortenUrlRepository;
        this.shortenBaseUrl = shortenBaseUrl;
    }

    public ShortenResponse shortenUrl(ShortenRequest shortenRequest){

        ShortenUrlEntity shortenUrlEntity = shortenUrlRepository.findByOriginalUrl(shortenRequest.getOriginalUrl());

        if(shortenUrlEntity != null){
            return buildShortenResponse(shortenUrlEntity.getShortUrl(), shortenUrlEntity.getOriginalUrl());
        }


        Long counter = shortenUrlRepository.getNextSequenceValue();
        String shortenUrl = shortenBaseUrl + convertToBase62(counter);
        saveShortenUrl(shortenUrl, shortenRequest.getOriginalUrl());
        return buildShortenResponse(shortenUrl, shortenRequest.getOriginalUrl());
    }

    private ShortenResponse buildShortenResponse(String shortenUrl, String originalUrl){
        ShortenResponse shortenResponse = new ShortenResponse();
        shortenResponse.setShortUrl(shortenUrl);
        shortenResponse.setOriginalUrl(originalUrl);
        return shortenResponse;
    }
    private void saveShortenUrl(String shortenUrl, String originalUrl){
        ShortenUrlEntity shortenUrlEntity = new ShortenUrlEntity();
        shortenUrlEntity.setShortUrl(shortenUrl);
        shortenUrlEntity.setOriginalUrl(originalUrl);
        shortenUrlRepository.save(shortenUrlEntity);
    }
    public String convertToBase62(long counter){
        StringBuilder encodedString = new StringBuilder();
        while (counter > 0){
            int indexAllowed = (int) (counter % base);
            encodedString.append(allowedCharacters[indexAllowed]);
            counter = counter / base;
        }
        return encodedString.reverse().toString();
    }
}
