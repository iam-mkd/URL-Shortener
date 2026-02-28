package com.example.urlshortener.repository;

import com.example.urlshortener.domain.entity.ClickEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClickEventRepository extends MongoRepository<ClickEvent, String> {

    long countByCode(String code);
}