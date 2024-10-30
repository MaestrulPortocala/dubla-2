package com.jamendo.model;

import java.util.List;

public class JamendoAlbumsResponse implements ResponseWithHeaders {
    private Headers headers;
    private List<Album> results;

    // Getters and Setters
    @Override
    public Headers getHeaders() {
        return headers;
    }
    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
    public List<Album> getResults() {
        return results;
    }
    public void setResults(List<Album> results) {
        this.results = results;
    }
}