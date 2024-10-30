package com.jamendo.model;

import java.util.List;

public class JamendoTracksResponse implements ResponseWithHeaders {
    private Headers headers;
    private List<Track> results;

    @Override
    public Headers getHeaders() {
        return headers;
    }
    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
    public List<Track> getResults() {
        return results;
    }
    public void setResults(List<Track> results) {
        this.results = results;
    }
}
