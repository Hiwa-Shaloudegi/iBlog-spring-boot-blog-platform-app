package dev.hiwa.iblog.exceptions;


public class ResourceNotFoundException extends RuntimeException {

    String resourceName;
    String field;
    String fieldName;


    public ResourceNotFoundException(
            String resourceName, String field, String fieldName
    ) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }
}