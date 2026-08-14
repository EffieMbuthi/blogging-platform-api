package com.BlogApp2.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class PostSummaryDto {
    private UUID id;
    private String title;
    private String bodyPreview;
    private AuthorSummaryDto author;
    private Set<String> tagNames;
    private int commentCount;
    private LocalDateTime createdAt;

    public PostSummaryDto() {}

    public PostSummaryDto(UUID id, String title, String bodyPreview, AuthorSummaryDto author,
                          Set<String> tagNames, int commentCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.bodyPreview = bodyPreview;
        this.author = author;
        this.tagNames = tagNames;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBodyPreview() { return bodyPreview; }
    public void setBodyPreview(String bodyPreview) { this.bodyPreview = bodyPreview; }
    public AuthorSummaryDto getAuthor() { return author; }
    public void setAuthor(AuthorSummaryDto author) { this.author = author; }
    public Set<String> getTagNames() { return tagNames; }
    public void setTagNames(Set<String> tagNames) { this.tagNames = tagNames; }
    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}