package com.ziqfaiz.url_shortener.service;

import com.ziqfaiz.url_shortener.entity.UrlMapping;
import com.ziqfaiz.url_shortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {
    private final UrlMappingRepository urlRepository;
    private static final String BASE62_CHARS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public UrlShortenerService(UrlMappingRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // Create a short URL
    public UrlMapping shortenUrl(String originalUrl) {
        // Check if URL already exists
        Optional<UrlMapping> existing = urlRepository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Create new mapping
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setCreatedAt(LocalDateTime.now());
        mapping = urlRepository.save(mapping);

        // Generate short code from ID
        String shortCode = encodeId(mapping.getId());
        mapping.setShortCode(shortCode);

        return urlRepository.save(mapping);
    }

    // Get original URL and record access
    public Optional<UrlMapping> getOriginalUrl(String shortCode) {
        Optional<UrlMapping> mapping = urlRepository.findByShortCode(shortCode);

        if (mapping.isPresent()) {
            UrlMapping url = mapping.get();
            url.setClickCount(url.getClickCount() + 1);
            url.setLastAccessedAt(LocalDateTime.now());
            urlRepository.save(url);
        }

        return mapping;
    }

    // Encode ID to base62
    private String encodeId(Long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(BASE62_CHARS.charAt((int)(id % 62)));
            id /= 62;
        }
        return sb.reverse().toString();
    }

    // Decode base62 back to ID (useful later)
    public Long decodeShortCode(String code) {
        Long id = 0L;
        for (char c : code.toCharArray()) {
            id = id * 62 + BASE62_CHARS.indexOf(c);
        }
        return id;
    }
}