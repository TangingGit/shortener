package com.example.shortener.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "shorten_url")
@Entity
public class ShortenUrlEntity {

    @Column(name = "short_url")
    private String shortUrl;
    @Id
    @Column(name = "original_url")
    private String originalUrl;
}
