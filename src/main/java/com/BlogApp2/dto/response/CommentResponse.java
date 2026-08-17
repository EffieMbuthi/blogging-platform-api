package com.BlogApp2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentResponse {
    private UUID id;
    private String body;
    private AuthorSummaryDto author;
    private LocalDateTime createdAt;

}