package com.BlogApp2.dto.response;

import java.util.UUID;

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