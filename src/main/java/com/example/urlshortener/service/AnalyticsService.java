package com.example.urlshortener.service;

import com.example.urlshortener.domain.entity.ClickEvent;
import com.example.urlshortener.repository.ClickEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AnalyticsService {

    private final ClickEventRepository repository;

    public AnalyticsService(ClickEventRepository repository) {
        this.repository = repository;
    }

    public void recordClick(String code, String userAgent, String ip) {
        ClickEvent event = new ClickEvent(code, Instant.now(), userAgent, ip);
        repository.save(event);
    }

    public long getClickCount(String code) {
        return repository.countByCode(code);
    }
}
