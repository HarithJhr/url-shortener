package com.harith.urlshortener.controller;

import com.harith.urlshortener.model.User;
import com.harith.urlshortener.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import com.harith.urlshortener.model.UrlMapping;
import com.harith.urlshortener.service.UrlMappingService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    private final UrlMappingService urlMappingService;

    public UserController(UserRepository userRepository, UrlMappingService urlMappingService) {
        this.userRepository = userRepository;
        this.urlMappingService = urlMappingService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OidcUser principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String googleId = principal.getAttribute("sub");

        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }

    @GetMapping("/my-links")
    public ResponseEntity<?> getMyLinks(@AuthenticationPrincipal OidcUser principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String googleId = principal.getSubject();

        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UrlMapping> links = urlMappingService.getLinksByUser(user);

        return ResponseEntity.ok(links);
    }
}

