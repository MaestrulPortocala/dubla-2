package com.jamendo;

import com.google.gson.*;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.locks.ReentrantLock;

public class TokenStorage {
    private static final String TOKEN_FILE = "access_token.json";
    private static final ReentrantLock lock = new ReentrantLock();

    public static void saveToken(String accessToken, long expiresAt) throws IOException {
        lock.lock();
        try {
            JsonObject tokenData = new JsonObject();
            tokenData.addProperty("access_token", accessToken);
            tokenData.addProperty("expires_at", expiresAt);

            try (Writer writer = Files.newBufferedWriter(Paths.get(TOKEN_FILE))) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(tokenData, writer);
            }
        } finally {
            lock.unlock();
        }
    }

    public static TokenData loadToken() throws IOException {
        lock.lock();
        try {
            if (!Files.exists(Paths.get(TOKEN_FILE))) {
                return null;
            }

            try (Reader reader = Files.newBufferedReader(Paths.get(TOKEN_FILE))) {
                Gson gson = new Gson();
                JsonObject tokenData = gson.fromJson(reader, JsonObject.class);

                if (tokenData == null) {
                    return null;
                }

                String accessToken = tokenData.get("access_token").getAsString();
                long expiresAt = tokenData.get("expires_at").getAsLong();

                return new TokenData(accessToken, expiresAt);
            }
        } finally {
            lock.unlock();
        }
    }

    public static void clearToken() throws IOException {
        lock.lock();
        try {
            Files.deleteIfExists(Paths.get(TOKEN_FILE));
        } finally {
            lock.unlock();
        }
    }
}