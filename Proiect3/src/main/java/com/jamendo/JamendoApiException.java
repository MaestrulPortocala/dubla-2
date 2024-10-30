package com.jamendo;

public class JamendoApiException extends Exception {
    public JamendoApiException(String message) {
        super(message);
    }

    public JamendoApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
