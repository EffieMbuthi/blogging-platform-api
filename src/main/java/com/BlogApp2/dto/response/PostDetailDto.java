package com.BlogApp2.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;


// has the full comments list, because this endpoint is specifically "give me everything about this one post."
public class PostDetailDto {
    private UUID id;
    private String title;
    private String body;
    private AuthorSummaryDto author;
    private Set<String> tagNames;
    private List<CommentResponse> comments;
    private double averageRating;
    private int reviewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //create an empty object first
    //no-args constructor → Jackson has no way to construct the object at all. IT NEEDS THIS.
    //needed for deserialization=> json- object
    public PostDetailDto() {}

    //entity(from the database)-→ DTO (building a response to send back out)
    // Not "DTO to entity and vice versa" as one interchangeable thing
    // — it's specifically the entity-to-DTO direction (outgoing).

    //what about the dto--> entity?
    //Instead, your Service layer reads the incoming DTO with GETTERS, and builds a brand-new Post entity
    public PostDetailDto(UUID id, String title, String body, AuthorSummaryDto author,
                         Set<String> tagNames, List<CommentResponse> comments,
                         double averageRating, int reviewCount,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.tagNames = tagNames;
        this.comments = comments;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

   //the setter isn't there for your code to call. It's there for Jackson to call,
   // on your behalf, as its mechanism for populating an object it just created
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public AuthorSummaryDto getAuthor() { return author; }
    public void setAuthor(AuthorSummaryDto author) { this.author = author; }
    public Set<String> getTagNames() { return tagNames; }
    public void setTagNames(Set<String> tagNames) { this.tagNames = tagNames; }
    public List<CommentResponse> getComments() { return comments; }
    public void setComments(List<CommentResponse> comments) { this.comments = comments; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}