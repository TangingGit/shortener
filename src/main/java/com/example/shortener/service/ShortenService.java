package com.example.shortener.service;

import com.example.shortener.model.request.ShortenRequest;
import com.example.shortener.model.response.shorten.ShortenResponse;
import com.example.shortener.repository.ShortenUrlRepository;
import com.example.shortener.repository.entity.ShortenUrlEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortenService {
    private static final String allowedString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final char[] allowedCharacters = allowedString.toCharArray();
    private final int base = allowedCharacters.length;
    private final ShortenUrlRepository shortenUrlRepository;
    private final String shortenBaseUrl;
    private final AuthenticationService authenticationService;
    ShortenService(
            ShortenUrlRepository shortenUrlRepository,
            @Value("${app.shorten.base-url}") String shortenBaseUrl,
            AuthenticationService authenticationService
    ){
        this.shortenUrlRepository = shortenUrlRepository;
        this.shortenBaseUrl = shortenBaseUrl;
        this.authenticationService = authenticationService;

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

    public String getRedirectUrl(String shortenUrl){
        ShortenUrlEntity shortenUrlEntity = shortenUrlRepository.findByShortUrl(shortenBaseUrl + shortenUrl);
        if(shortenUrlEntity != null){
            return shortenUrlEntity.getOriginalUrl();
        } else {
            throw new RuntimeException("URL not found");
        }
    }

    public List<ShortenUrlEntity> getAllUrls(String token){
        authenticationService.validateToken(token.split("Bearer ")[1]);
        return shortenUrlRepository.findAll();
    }

    public void deleteUrl(String token, Long id){
        authenticationService.validateToken(token.split("Bearer ")[1]);
        shortenUrlRepository.deleteById(id);
    }
}
