package com.BlogApp2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagRequest {

    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 30, message = "Tag name must be between 2 and 30 characters")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}