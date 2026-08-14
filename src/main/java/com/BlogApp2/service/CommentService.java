package com.BlogApp2.service;

import com.BlogApp2.dto.request.CommentRequest;
import com.BlogApp2.dto.response.CommentResponse;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    CommentResponse createComment(UUID postId, CommentRequest request);
    List<CommentResponse> getCommentsByPost(UUID postId);
    void deleteComment(UUID postId, UUID commentId);
}