package com.BlogApp2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CommentRequest{
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Comment body is required")
    @Size(min = 1, max = 500, message = "Comment must be between 1 and 500 characters")
    private String body;


    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}