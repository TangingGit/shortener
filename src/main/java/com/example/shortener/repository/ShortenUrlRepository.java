package com.example.shortener.repository;

import com.example.shortener.repository.entity.ShortenUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ShortenUrlRepository extends JpaRepository<ShortenUrlEntity, String> {
    @Query(value = "SELECT NEXTVAL(base62_counter_seq);", nativeQuery = true)
    Long getNextSequenceValue();

    ShortenUrlEntity findByOriginalUrl(String originalUrl);
}