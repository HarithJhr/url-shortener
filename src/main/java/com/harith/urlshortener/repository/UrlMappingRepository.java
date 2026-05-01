package com.harith.urlshortener.repository;

import com.harith.urlshortener.model.UrlMapping;
import com.harith.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortCode(String shortCode);

    List<UrlMapping> findByUser(User user);

    Optional<UrlMapping> findByIdAndUser(Long id, User user);
}