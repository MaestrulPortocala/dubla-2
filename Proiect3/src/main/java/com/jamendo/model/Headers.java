package com.jamendo.model;

public class Headers {
    private String status;
    private int code;
    private String error_message;
    private String warnings;
    private int results_count;
    private String next;

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getError_message() {
        return error_message;
    }

    public String getWarnings() {
        return warnings;
    }

    public int getResults_count() {
        return results_count;
    }

    public String getNext() {
        return next;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public void setResults_count(int results_count) {
        this.results_count = results_count;
    }

    public void setNext(String next) {
        this.next = next;
    }
}