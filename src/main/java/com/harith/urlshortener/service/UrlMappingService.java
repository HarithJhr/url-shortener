package com.harith.urlshortener.service;

import com.harith.urlshortener.dto.CreateShortUrlResponse;
import com.harith.urlshortener.model.UrlMapping;
import com.harith.urlshortener.model.User;
import com.harith.urlshortener.repository.UrlMappingRepository;
import com.harith.urlshortener.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlMappingService {

    private final UrlMappingRepository repository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Value("${app.base-url}")
    private String appBaseUrl;

    public UrlMappingService(UrlMappingRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public CreateShortUrlResponse createShortUrl(String longUrl, String googleId) {

        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String shortCode = generateUniqueShortCode();

        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl(longUrl);
        mapping.setShortCode(shortCode);
        mapping.setClickCount(0L);

        mapping.setUser(user);

        repository.save(mapping);

        String shortUrl = appBaseUrl + "/" + shortCode;

        return new CreateShortUrlResponse(shortCode, shortUrl, longUrl);
    }

    public Optional<UrlMapping> getByShortCode(String shortCode) {
        return repository.findByShortCode(shortCode);
    }

    public List<UrlMapping> getAllUrls() {
        return repository.findAll();
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

    public List<UrlMapping> getLinksByUser(User user) {
        return repository.findByUser(user);
    }

    public boolean deleteUrlForUser(Long id, User user) {
        Optional<UrlMapping> urlMapping = repository.findByIdAndUser(id, user);

        if (urlMapping.isEmpty()) {
            return false;
        }

        repository.delete(urlMapping.get());

        return true;
    }
}