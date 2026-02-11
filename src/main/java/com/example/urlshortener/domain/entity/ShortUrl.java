package com.example.urlshortener.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;

@Entity
@Table(name = "short_urls", indexes = {
    @Index(name = "idx_code", columnList = "code", unique = true)
})
@Data
public class ShortUrl {

    @Id
    private Long id;


    @Column(nullable = false, unique = true, length = 16)
    private String code;

    @Column(nullable = false, length = 2048)
    private String longUrl;

    private Instant createdAt = Instant.now();
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
