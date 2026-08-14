package com.BlogApp2.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentResponse {
    private UUID id;
    private String body;
    private AuthorSummaryDto author;
    private LocalDateTime createdAt;

    public CommentResponse() {}

    public CommentResponse(UUID id, String body, AuthorSummaryDto author, LocalDateTime createdAt) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public AuthorSummaryDto getAuthor() { return author; }
    public void setAuthor(AuthorSummaryDto author) { this.author = author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}