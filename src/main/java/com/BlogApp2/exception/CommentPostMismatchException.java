package com.BlogApp2.exception;

import java.util.UUID;

public class CommentPostMismatchException extends RuntimeException {
    public CommentPostMismatchException(UUID commentId, UUID postId) {
        super("Comment " + commentId + " does not belong to post " + postId);
    }
}