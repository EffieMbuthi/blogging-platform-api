package com.BlogApp2.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewResponse{
    private UUID id;
    private AuthorSummaryDto author;
    private int rating;
    private LocalDateTime createdAt;

    public ReviewResponse() {}

    public ReviewResponse(UUID id, AuthorSummaryDto author, int rating, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public AuthorSummaryDto getAuthor() { return author; }
    public void setAuthor(AuthorSummaryDto author) { this.author = author; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}