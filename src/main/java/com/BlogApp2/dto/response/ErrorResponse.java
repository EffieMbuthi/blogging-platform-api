package com.BlogApp2.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private int status;
    private String message;

    //it checks every constraint on the object and collects all the violations at once.
    //So in this one request, there are genuinely two separate failures:
    //title failed @NotBlank, and body failed @NotBlank,
    //each with its own distinct message ("Title is required" vs "A body is required").
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    public ErrorResponse() {}

    public ErrorResponse(int status, String message, Map<String, String> errors, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    // convenience constructor for the common case: no field-level errors
    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this(status, message, null, timestamp);
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}