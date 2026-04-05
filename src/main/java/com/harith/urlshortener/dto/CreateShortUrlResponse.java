package com.harith.urlshortener.dto;

public class CreateShortUrlResponse {
    private String shortCode;
    private String shortUrl;
    private String longUrl;

    public CreateShortUrlResponse(String shortCode, String shortUrl, String longUrl) {
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }

    public String getShortCOde() {
        return shortCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }
}