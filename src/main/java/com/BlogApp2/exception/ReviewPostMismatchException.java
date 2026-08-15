package com.BlogApp2.exception;

import java.util.UUID;

public class ReviewPostMismatchException extends RuntimeException {
    public ReviewPostMismatchException(UUID reviewId, UUID postId) {
        super("Review " + reviewId + " does not belong to post " + postId);
    }
}