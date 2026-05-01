package com.harith.urlshortener.controller;

import com.harith.urlshortener.dto.CreateShortUrlRequest;
import com.harith.urlshortener.model.UrlMapping;
import com.harith.urlshortener.model.User;
import com.harith.urlshortener.repository.UserRepository;
import com.harith.urlshortener.service.UrlMappingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final UserRepository userRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public UrlMappingController(
            UrlMappingService service,
            UserRepository userRepository
    ) {
        this.service = service;
        this.userRepository = userRepository;
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
    public ResponseEntity<?> deleteUrl(
            @PathVariable Long id,
            @AuthenticationPrincipal OidcUser oidcUser
    ) {
        User user = userRepository.findByEmail(oidcUser.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean urlExists = service.deleteUrlForUser(id, user);

        if (!urlExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "URL not found"));
        }

        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }
}