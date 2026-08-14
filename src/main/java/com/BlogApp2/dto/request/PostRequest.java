package com.BlogApp2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PostRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "A body is required")
    @Size(min = 5, max = 500, message = "Body must be between 5 and 500 characters")
    private String body;

    private Set<String> tagNames = new HashSet<>();

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public Set<String> getTagNames() { return tagNames; }
    public void setTagNames(Set<String> tagNames) { this.tagNames = tagNames; }
}