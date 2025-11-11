package com.example.shortener.repository;

import com.example.shortener.repository.entity.ShortenUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShortenUrlRepository extends JpaRepository<ShortenUrlEntity, String> {

}