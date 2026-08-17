package com.BlogApp2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostSummaryDto {
    private UUID id;
    private String title;
    private String bodyPreview;
    private AuthorSummaryDto author;
    private Set<String> tagNames;
    private int commentCount;
    private LocalDateTime createdAt;
}