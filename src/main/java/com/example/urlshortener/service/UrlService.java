package com.example.urlshortener.service;

import com.example.urlshortener.domain.entity.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.util.Base62Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UrlService {
    private final ShortUrlRepository repository;
    private final SnowflakeIdGenerator idGenerator;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.cache-ttl-seconds}")
    private long cacheTtl;

    public UrlService(ShortUrlRepository repository,
                      SnowflakeIdGenerator idGenerator,
                      StringRedisTemplate redisTemplate) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.redisTemplate = redisTemplate;
    }

    public ShortUrl createShortUrl(String longUrl, String customAlias, Instant expiresAt) {
        String code;

    if (customAlias != null && !customAlias.isBlank()) {
        // check if alias already exists
        if (repository.findByCode(customAlias).isPresent()) {
            throw new IllegalArgumentException("Custom alias already in use");
        }
        code = customAlias;
    } else {
        long id = idGenerator.nextId();
        code = Base62Encoder.encode(id);
    }

    Long id = idGenerator.nextId(); // still keep snowflake id as _id
    ShortUrl entity = new ShortUrl(id, code, longUrl, expiresAt);
    repository.save(entity);

    cache(code, longUrl);
    return entity;
    }

    public Optional<String> resolve(String code) {
        String cacheKey = "url:" + code;

        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Optional.of(cached);
        }

        Optional<ShortUrl> entityOpt = repository.findByCode(code);
        if (entityOpt.isEmpty()) return Optional.empty();

        ShortUrl entity = entityOpt.get();
        if (!entity.isActive()) return Optional.empty();
        if (entity.getExpiresAt() != null && entity.getExpiresAt().isBefore(Instant.now())) {
            return Optional.empty();
        }

        cache(code, entity.getLongUrl());
        return Optional.of(entity.getLongUrl());
    }

    private void cache(String code, String longUrl) {
        redisTemplate.opsForValue().set("url:" + code, longUrl, cacheTtl, TimeUnit.SECONDS);
    }
}
