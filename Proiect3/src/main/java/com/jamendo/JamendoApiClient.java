package com.jamendo;

import okhttp3.*;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Map;
import com.jamendo.model.*;

public class JamendoApiClient {
    private static final Logger logger = LoggerFactory.getLogger(JamendoApiClient.class);
    private final OkHttpClient httpClient;
    private final Gson gson;

    public JamendoApiClient() {
        this.httpClient = new OkHttpClient.Builder()
                .callTimeout(java.time.Duration.ofSeconds(30))
                .build();
        this.gson = new Gson();
    }

    public JamendoAlbumsResponse getAlbums(Map<String, String> queryParams) throws JamendoApiException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.API_BASE_URL + "/albums").newBuilder();
        urlBuilder.addQueryParameter("client_id", Config.CLIENT_ID);
        urlBuilder.addQueryParameter("format", "json");

        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpUrl url = urlBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        JamendoAlbumsResponse albumsResponse = executeRequest(request, JamendoAlbumsResponse.class);

        return albumsResponse;
    }

    public JamendoTracksResponse getTracks(Map<String, String> queryParams) throws JamendoApiException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.API_BASE_URL + "/tracks").newBuilder();
        urlBuilder.addQueryParameter("client_id", Config.CLIENT_ID);
        urlBuilder.addQueryParameter("format", "json");

        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpUrl url = urlBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        JamendoTracksResponse tracksResponse = executeRequest(request, JamendoTracksResponse.class);

        return tracksResponse;
    }

    private <T extends ResponseWithHeaders> T executeRequest(Request request, Class<T> responseType) throws JamendoApiException {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No response body";
                logger.error("API request failed: HTTP {} - {}", response.code(), errorBody);
                throw new JamendoApiException("API request failed: " + response.code() + " - " + errorBody);
            }
            String responseBody = response.body().string();
            T parsedResponse = gson.fromJson(responseBody, responseType);

            if (!"success".equalsIgnoreCase(parsedResponse.getHeaders().getStatus())) {
                String errorMessage = parsedResponse.getHeaders().getError_message();
                throw new JamendoApiException("API responded with error: " + errorMessage);
            }

            return parsedResponse;
        } catch (IOException | JsonParseException e) {
            logger.error("Failed to execute API request", e);
            throw new JamendoApiException("Failed to execute API request", e);
        }
    }
}