package com.example.demo.config;

import com.example.demo.dto.ApiResponse;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> b));
        return ApiResponse.error("Validation failed: " + errors);
    }

    // IllegalArgumentException is for programming errors (bad input) — 400
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleIllegalArg(IllegalArgumentException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    // RuntimeException covers all our domain errors (seat unavailable, duplicate booking, etc.) — 400
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleRuntime(RuntimeException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    // NoSuchElementException from .orElseThrow() without a message — 404
    @ExceptionHandler(java.util.NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNotFound(java.util.NoSuchElementException ex) {
        return ApiResponse.error("Resource not found.");
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleAccess() {
        return ApiResponse.error("Access denied. Please login first.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGeneral(Exception ex) {
        // Log in real app: log.error("Unhandled exception", ex);
        return ApiResponse.error("Something went wrong. Please try again.");
    }
}
