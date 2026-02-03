package org.example.exception;

public class InvalidPINException extends RuntimeException {

    public InvalidPINException(String message) {
        super(message);
    }

    public InvalidPINException(String message, Throwable cause) {
        super(message, cause);
    }
}
