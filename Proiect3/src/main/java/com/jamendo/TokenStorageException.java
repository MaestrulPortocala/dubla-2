package com.jamendo;

public class TokenStorageException extends Exception {
    public TokenStorageException(String message) {
        super(message);
    }

    public TokenStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
