package com.example.shortener.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.shortener.exception.IdentifyException;
import com.example.shortener.exception.JwtInvalidException;
import com.example.shortener.exception.UserExistException;
import com.example.shortener.model.request.RegisterRequest;
import com.example.shortener.model.response.authentication.LoginResponse;
import com.example.shortener.repository.UserRepository;
import com.example.shortener.repository.entity.UserEntity;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final String rsaPrivateKey;
    private final String rsaPublicKey;
    AuthenticationService(
            UserRepository userRepository,
            @Value("${app.auth.private-key}") String rsaPrivateKey,
            @Value("${app.auth.public-key}") String rsaPublicKey
    ) {
        this.userRepository = userRepository;
        this.rsaPrivateKey = rsaPrivateKey;
        this.rsaPublicKey = rsaPublicKey;
    }


    public LoginResponse login(RegisterRequest loginRequest){
        identifyUser(loginRequest.getEmail(), loginRequest.getPassword());
        return buildLoginResponse(getToken(loginRequest.getEmail()));

    }

    public String getToken(String email){
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
        Optional<UserEntity> userEntityResult = userRepository.findById(registerRequest.getEmail());
        if (userEntityResult.isPresent()){
            throw new UserExistException();
        }
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
