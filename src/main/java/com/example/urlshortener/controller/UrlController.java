package com.example.urlshortener.controller;

import com.example.urlshortener.domain.entity.ShortUrl;
import com.example.urlshortener.service.AnalyticsService;
import com.example.urlshortener.service.RateLimiterService;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;
    private final AnalyticsService analyticsService;
    private final RateLimiterService rateLimiterService;

    public UrlController(UrlService urlService,
                         AnalyticsService analyticsService,
                         RateLimiterService rateLimiterService) {
        this.urlService = urlService;
        this.analyticsService = analyticsService;
        this.rateLimiterService = rateLimiterService;
    }

    // 🔹 Create Short URL
    @PostMapping("/api/v1/urls")
    public ResponseEntity<?> createShortUrl(@RequestBody Map<String, String> body,
                                            HttpServletRequest request) {

        String ip = request.getRemoteAddr();

        // Optional rate limiting (100 requests per minute)
        if (!rateLimiterService.isAllowed(ip, 100, 60)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate limit exceeded");
        }

        String longUrl = body.get("longUrl");
        String customAlias = body.get("customAlias");
        Instant expiresAt = body.containsKey("expiresAt")
                ? Instant.parse(body.get("expiresAt"))
                : null;

        ShortUrl shortUrl = urlService.createShortUrl(
                longUrl,
                customAlias,
                expiresAt
        );

        return ResponseEntity.ok(Map.of(
                "code", shortUrl.getCode(),
                "shortUrl", "http://localhost:8080/" + shortUrl.getCode()
        ));
    }

    // 🔹 Redirect
    @GetMapping("/{code}")
    public void redirect(@PathVariable String code,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        var resolved = urlService.resolve(code);
        if (resolved.isEmpty()) {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return;
        }

        // Record click analytics
        analyticsService.recordClick(
                code,
                request.getHeader("User-Agent"),
                request.getRemoteAddr()
        );

        response.sendRedirect(resolved.get());
    }

    // 🔹 Get Click Stats
    @GetMapping("/api/v1/urls/{code}/stats")
    public ResponseEntity<?> getStats(@PathVariable String code) {

        long clickCount = analyticsService.getClickCount(code);

        return ResponseEntity.ok(Map.of(
                "code", code,
                "clickCount", clickCount
        ));
    }
}