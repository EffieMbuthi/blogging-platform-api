package com.BlogApp2.dto.response;

import java.util.UUID;

//a stranger reading a blog post has no legitimate reason to see the author's email address
//is what gets nested inside a post/comment, where only "who wrote this" matters, not their contact info.
public class AuthorSummaryDto {
    private UUID id;
    private String name;

    public AuthorSummaryDto() {}

    public AuthorSummaryDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}