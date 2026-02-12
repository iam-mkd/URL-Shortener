package com.example.urlshortener.repository;

import com.example.urlshortener.domain.entity.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShortUrlRepository extends MongoRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByCode(String code);
    
}
