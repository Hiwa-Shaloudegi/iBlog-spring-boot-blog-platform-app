package dev.hiwa.iblog.exceptions;

public class ResourceConstraintViolationException extends RuntimeException {

    public ResourceConstraintViolationException(String message) {
        super(message);
    }
}
