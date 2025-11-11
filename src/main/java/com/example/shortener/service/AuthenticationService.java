package com.example.shortener.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.shortener.exception.IdentifyException;
import com.example.shortener.exception.JwtInvalidException;
import com.example.shortener.model.request.RegisterRequest;
import com.example.shortener.model.response.authentication.LoginResponse;
import com.example.shortener.repository.UserRepository;
import com.example.shortener.repository.entity.UserEntity;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    AuthenticationService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    private final String rsaPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDFjMxDpDJNoCghXJphN1IGE4ahiOww7yhAuGErpxltDnwJO+F01AbIfsujwcUIXZ/Wr02pnJtlwZbBK3UPGp1fkjFVrtTnEDyIWMoQrT2jmNGKxBXpkmce8O2zyonq1krVea/Q0tDydvUJhpMTYKM730H+XKDIhZF3qwaJkH4ORvyQMZhx9TSWoi3SMOSgEhUs3YJKzC6s9FEd5JZHuqNTrWRUG6KFfPsv0EQjrVOtHSaaDqLpwZcXChAD2rMyCjaFCbNGbxamm2gBiwQCCWp3LZwWp8PcPBjIKdkWNzrDhruc1qAHGoqyhXZ/zLujHyg1dWlmXMzFU9dFKh4tkFBJAgMBAAECggEADqUFEM/09AY3E19kXmfrH7eGLSa+WP5Rs0KNNDQ7UUytQJinelwBc2QwP8qB4Gs4XyzjCqYpFZ5dSPf/bIg3jw8tCGy2gblAE4d+1e+nIx/qthPFNWnR/9wycuKaiMXWtFlErFavK1pK3f2TnPB2tAiKluBSVwB31obfgYaBocMwu+sj6yVX/4QQ61W0acvNGDoL4xFEOaHRiqAj3q/DTHgwtZ7bUdYCOMf0LgyPoZrk7dw2k9e3Zp1kpaJKd3Q50PqJsbbDXBNaStnYXIVwOyskXKlsITgAjQfSCRek7/3fxEDB9QqWQ29fxe8PhRJn9WFq8+73wyZ3n8sNBMKXFQKBgQDi+wQdXZTpUul/jXR8KQKK6WWph3BU2SGww8qOHkexp18/wOEllIxVsz1jzHyHit00+03rn113GN5PeeCQ89VbOckacb5DNb12Low9kEccQ8qnmLBGyigdN0fCL9v7pHwGtMg1A2wnaqJg33XgwOjjwj2p/WemL6BTbH4Rph7C8wKBgQDezogjip93HMulFCxA3cP78DkwVTRwTmtfx5jNeJh60hPxXhWJLqBN3onMMbRV/MZjXK+V096AFl4qXNKOWOcjR4KRecVxTMmV87pVWJCTjD4KwaLJbI//uhrImkjfajWhCNa+4IYdRKyfxWQlFf7ZsBNst1nzaea6Fr5y3mhW0wKBgA6RKYxw6xOmXJHxFB67ywv7OqkMpirr3HuptSA11UgTg8tlp41v4SnG3RPAleVlBhlWX4PVsHvqPqo/GihzXagDJ2LpFdreYhBpoFp7HfU0bJcg938zjiuB1rshsy6NOeB8TbUJSTGmaFFrRfdsqgWj+60jNDm4KMPx3I1paeP9AoGBANiIx+4irs/IAHIypL91xx52XF4IsKnkzJ2NEYe/CBF1UURmN2/XDJwhBrKYK7bVrsiqL0rhWYAKPQHWrBsDc77ppI9VVPnDsrY05IZFSFzdEv7JKTewdlIFwDntF51pTy1ttaZT9oNDVrD/2U/hjIhrgrVJi/XwmfeIWxav559ZAoGAdcRhDqa6cXdsMvdDc26CM1/yFpTa5PkI796A3rc1YomKA0duaWYuZrYT+o/7taKnB5VQ9uZHXP8dfUFMWICIj/m9yEwYt0anAbL5YvUiSiIfMSZfN++ghpfSjEdBSC2SAmpkk/NPM3Zw2bLkON1r1lylWrAex6AQynjJQMIS9Ao=";
    private final String rsaPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxYzMQ6QyTaAoIVyaYTdSBhOGoYjsMO8oQLhhK6cZbQ58CTvhdNQGyH7Lo8HFCF2f1q9NqZybZcGWwSt1DxqdX5IxVa7U5xA8iFjKEK09o5jRisQV6ZJnHvDts8qJ6tZK1Xmv0NLQ8nb1CYaTE2CjO99B/lygyIWRd6sGiZB+Dkb8kDGYcfU0lqIt0jDkoBIVLN2CSswurPRRHeSWR7qjU61kVBuihXz7L9BEI61TrR0mmg6i6cGXFwoQA9qzMgo2hQmzRm8WpptoAYsEAglqdy2cFqfD3DwYyCnZFjc6w4a7nNagBxqKsoV2f8y7ox8oNXVpZlzMxVPXRSoeLZBQSQIDAQAB";
    public LoginResponse login(RegisterRequest loginRequest){
        identifyUser(loginRequest.getEmail(), loginRequest.getPassword());
        return buildLoginResponse(getToken(loginRequest.getEmail()));

    }

    private String getToken(String email){
        try {
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpecPrivate);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpecPublic);
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);

            return JWT.create()
                    .withExpiresAt(new java.util.Date(System.currentTimeMillis() + 3600 * 1000))
                    .withClaim("email", email)
                    .sign(algorithm);
        } catch (JWTCreationException | NoSuchAlgorithmException | InvalidKeySpecException e){
            log.error("e: ", e);
            throw new RuntimeException(e);
        }
    }
    private LoginResponse buildLoginResponse(String token){
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
    }

    private void identifyUser(String email, String password) {
        String passwordHash = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();

        UserEntity userEntity = userRepository.findByEmailAndPassword(email, passwordHash);

        if (userEntity == null) {
            throw new IdentifyException();
        }

    }

    public void register(RegisterRequest registerRequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerRequest.getEmail());

        String passwordHash = Hashing.sha256()
                .hashString(registerRequest.getPassword(), StandardCharsets.UTF_8)
                .toString();
        userEntity.setPassword(passwordHash);
        userRepository.save(userEntity);
    }

    public void validateToken(String token){
        try {
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpecPrivate);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpecPublic);
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);

            JWT.require(algorithm)
                    .build()
                    .verify(token);
        } catch (Exception e){
            log.error("e: ", e);
            throw new JwtInvalidException();
        }
    }
}
