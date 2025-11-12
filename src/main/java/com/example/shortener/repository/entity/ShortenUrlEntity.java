package com.example.shortener.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "shorten_url")
@Entity
public class ShortenUrlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "short_url")
    private String shortUrl;
    @Column(name = "original_url")
    private String originalUrl;
}
