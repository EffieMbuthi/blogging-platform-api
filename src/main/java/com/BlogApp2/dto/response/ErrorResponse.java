package com.BlogApp2.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;


@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    private int status;
    private String message;

    //it checks every constraint on the object and collects all the violations at once.
    //So in this one request, there are genuinely two separate failures:
    //title failed @NotBlank, and body failed @NotBlank,
    //each with its own distinct message ("Title is required" vs "A body is required").
    private Map<String, String> errors;
    private LocalDateTime timestamp;


    //full version
    public ErrorResponse(int status, String message, Map<String, String> errors, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    // convenience constructor for the common case: no field-level errors
    //short version
    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this(status, message, null, timestamp);
    }
}