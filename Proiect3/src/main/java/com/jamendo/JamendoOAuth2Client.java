package com.jamendo;

import okhttp3.*;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class JamendoOAuth2Client {
    private static final Logger logger = LoggerFactory.getLogger(JamendoOAuth2Client.class);
    private static final String AUTHORIZATION_URL = Config.API_BASE_URL + "/oauth/authorize";
    private static final String TOKEN_URL = Config.API_BASE_URL + "/oauth/grant";
    private final OkHttpClient httpClient;

    private String accessToken;
    private long expiresAt; // Timestamp in milliseconds when the token expires

    public JamendoOAuth2Client() {
        this.httpClient = new OkHttpClient.Builder()
                .callTimeout(java.time.Duration.ofSeconds(30))
                .build();

        // Load the access token and expiration date from storage
        try {
            TokenData tokenData = TokenStorage.loadToken();
            if (tokenData != null) {
                this.accessToken = tokenData.getAccessToken();
                this.expiresAt = tokenData.getExpiresAt();
                logger.info("Access token loaded from storage. Expires at: {}", this.expiresAt);
            } else {
                this.accessToken = null;
                this.expiresAt = 0;
            }
        } catch (IOException e) {
            logger.error("Failed to load access token from storage", e);
            this.accessToken = null;
            this.expiresAt = 0;
        }
    }

    /**
     * Generate the authorization URL to direct the user to.
     */
    public String getAuthorizationUrl() throws IOException {
        String redirectUri = "http://localhost"; // Redirect URI registered with Jamendo

        String url = AUTHORIZATION_URL + "?" +
                "client_id=" + URLEncoder.encode(Config.CLIENT_ID, StandardCharsets.UTF_8.name()) +
                "&response_type=code" +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.name()) +
                "&scope=" + URLEncoder.encode(Config.SCOPE, StandardCharsets.UTF_8.name());

        return url;
    }

    /**
     * Exchange authorization code for access token.
     */
    public void exchangeAuthorizationCode(String code) throws JamendoApiException {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Authorization code must not be null or empty");
        }

        RequestBody formBody = new FormBody.Builder()
                .add("client_id", Config.CLIENT_ID)
                .add("client_secret", Config.CLIENT_SECRET)
                .add("grant_type", "authorization_code")
                .add("code", code.trim())
                .add("redirect_uri", "http://localhost")
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(formBody)
                .build();

        executeTokenRequest(request);
    }

    /**
     * Execute token request and parse response.
     */
    private void executeTokenRequest(Request request) throws JamendoApiException {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No response body";
                logger.error("Token request failed: HTTP {} - {}", response.code(), errorBody);
                throw new JamendoApiException("Failed to obtain tokens: HTTP " + response.code() + " - " + errorBody);
            }

            String responseBody = response.body().string();
            parseTokenResponse(responseBody);

            // Save the access token and expiration date to storage
            try {
                TokenStorage.saveToken(this.accessToken, this.expiresAt);
                logger.info("Access token saved to storage.");
            } catch (IOException e) {
                logger.error("Failed to save access token to storage", e);
            }

        } catch (IOException e) {
            logger.error("Token request failed due to network error", e);
            throw new JamendoApiException("Network error while obtaining tokens", e);
        }
    }

    /**
     * Parse the token response and update tokens.
     */
    private void parseTokenResponse(String responseBody) throws JamendoApiException {
        try {
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            this.accessToken = jsonObject.get("access_token").getAsString();
            int expiresIn = jsonObject.get("expires_in").getAsInt();
            this.expiresAt = System.currentTimeMillis() + (expiresIn * 1000L);

            logger.info("Access token obtained successfully. Expires at: {}", this.expiresAt);

        } catch (JsonParseException | NullPointerException e) {
            logger.error("Failed to parse token response", e);
            throw new JamendoApiException("Failed to parse tokens", e);
        }
    }

    /**
     * Get the current access token, request a new one if expired.
     */
    public String getValidAccessToken() throws JamendoApiException {
        if (accessToken == null || isAccessTokenExpired()) {
            throw new JamendoApiException("Access token is not available or has expired. Please authorize again.");
        }
        return accessToken;
    }

    /**
     * Check if the access token has expired.
     */
    private boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > expiresAt;
    }

    /**
     * Clears stored tokens.
     */
    public void clearTokens() {
        accessToken = null;
        expiresAt = 0;
        // Delete the token file
        try {
            TokenStorage.clearToken();
            logger.info("Access token has been cleared from storage.");
        } catch (IOException e) {
            logger.error("Failed to clear access token from storage", e);
        }
    }
}
