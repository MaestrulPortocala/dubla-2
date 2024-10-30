package com.jamendo;

public class TokenData {
    private final String accessToken;
    private final long expiresAt;

    public TokenData(String accessToken, long expiresAt) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > expiresAt;
    }
}
