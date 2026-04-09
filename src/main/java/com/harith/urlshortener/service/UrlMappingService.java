package com.harith.urlshortener.service;

import com.harith.urlshortener.model.UrlMapping;
import com.harith.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlMappingService {

    private final UrlMappingRepository repository;
    private final Random random = new Random();

    public UrlMappingService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public UrlMapping createShortUrl(String longUrl) {
        String shortCode = generateUniqueShortCode();
        UrlMapping urlMapping = new UrlMapping(longUrl, shortCode);
        return repository.save(urlMapping);
    }

    public Optional<UrlMapping> getByShortCode(String shortCode) {
        return repository.findByShortCode(shortCode);
    }

    public List<UrlMapping> getAllUrls() {
        return repository.findAll();
    }

    public boolean isValidId(Long id) {
        return repository.findById(id).isPresent();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void incrementClickCount(UrlMapping urlMapping) {
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        repository.save(urlMapping);
    }

    private String generateUniqueShortCode() {
        String shortCode;

        do {
            shortCode = generateRandomCode(6);
        } while (repository.findByShortCode(shortCode).isPresent());

        return shortCode;
    }

    private String generateRandomCode(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }
}