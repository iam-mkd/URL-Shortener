package com.example.urlshortener.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Document(collection = "click_events")
public class ClickEvent {

    @Id
    private String id;

    private String code;

    @CreatedDate
    private Instant timestamp;

    private String userAgent;
    private String ip;

    protected ClickEvent() {}

    public ClickEvent(String code, Instant timestamp, String userAgent, String ip) {
        this.code = code;
        this.timestamp = timestamp;
        this.userAgent = userAgent;
        this.ip = ip;
    }
}
