package com.example.urlshortener.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "short_urls")
@Data
public class ShortUrl {

    @Id
    private Long id;


    @Indexed(unique = true)
    private String code;

    
    private String longUrl;

    private Instant createdAt = Instant.now();

    // 👇 TTL index: Mongo will delete doc when this time is reached
    @Indexed(expireAfterSeconds = 0)
    private Instant expiresAt;
    
    private boolean active = true;

    protected ShortUrl() {}

    public ShortUrl(Long id, String code, String longUrl, Instant expiresAt) {
        this.id = id;
        this.code = code;
        this.longUrl = longUrl;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    
}
