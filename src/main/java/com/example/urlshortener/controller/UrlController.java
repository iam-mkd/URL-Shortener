package com.example.urlshortener.controller;

import com.example.urlshortener.domain.entity.ShortUrl;
import com.example.urlshortener.service.UrlService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class UrlController {

    @Autowired
    private UrlService service;

    @PostMapping("/api/v1/urls")
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) throws Exception {
        String longUrl = body.get("longUrl");

        ShortUrl shortUrl = service.createShortUrl(longUrl, null);

        return ResponseEntity.ok(Map.of(
                "code", shortUrl.getCode(),
                "shortUrl", "http://localhost:8080/" + shortUrl.getCode()
        ));
    }

    @GetMapping("/{code}")
    public void redirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        var resolved = service.resolve(code);
        if (resolved.isEmpty()) {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return;
        }
        response.sendRedirect(resolved.get());
    }
}

