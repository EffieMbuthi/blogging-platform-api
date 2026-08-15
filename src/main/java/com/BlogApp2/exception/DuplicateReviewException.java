package com.BlogApp2.exception;

import java.util.UUID;

public class DuplicateReviewException extends RuntimeException {
    public DuplicateReviewException(UUID postId, UUID userId) {
        super("User " + userId + " has already reviewed post " + postId);
    }
}