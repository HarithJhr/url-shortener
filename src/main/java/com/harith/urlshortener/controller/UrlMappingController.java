package com.harith.urlshortener.controller;

import com.harith.urlshortener.dto.CreateShortUrlRequest;
import com.harith.urlshortener.dto.CreateShortUrlResponse;
import com.harith.urlshortener.model.UrlMapping;
import com.harith.urlshortener.service.UrlMappingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlMappingController {

    private final UrlMappingService service;

    @Value("${app.base-url}")
    private String baseUrl;

    public UrlMappingController(UrlMappingService service) {
        this.service = service;
    }

    @PostMapping("/api/urls")
    public ResponseEntity<?> createShortUrl(
            @RequestBody CreateShortUrlRequest request,
            @AuthenticationPrincipal OidcUser principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String googleId = principal.getSubject();

        return ResponseEntity.ok(
                service.createShortUrl(request.getLongUrl(), googleId)
        );
    }

    @GetMapping("/{shortCode:[a-zA-Z0-9]{6,}}")
    public void redirectToLongUrl(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        Optional<UrlMapping> result = service.getByShortCode(shortCode);

        if (result.isPresent()) {
            UrlMapping urlMapping = result.get();
            service.incrementClickCount(urlMapping);
            response.sendRedirect(urlMapping.getLongUrl());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Short URL not found");
        }
    }

    @GetMapping("/api/urls")
    public List<UrlMapping> getAllUrls() {
        return service.getAllUrls();
    }

    @DeleteMapping("/api/urls/{id}")
    public ResponseEntity<?> deleteUrl(@PathVariable Long id) {
        boolean isValidId = service.isValidId(id);

        if (isValidId) {
            service.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
        } else  {
            return ResponseEntity.status(404).body(Map.of("error", "id not found"));
        }

    }
}