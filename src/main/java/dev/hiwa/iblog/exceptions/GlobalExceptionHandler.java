package dev.hiwa.iblog.exceptions;

import dev.hiwa.iblog.domain.dto.response.ApiErrorResponse;
import dev.hiwa.iblog.domain.dto.response.ApiValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);

        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

        return ResponseEntity.internalServerError().body(apiErrorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiValidationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex
                .getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));


        ApiValidationErrorResponse apiValidationErrorResponse = ApiValidationErrorResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Invalid input data")
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(apiValidationErrorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex
    ) {
        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }
}
