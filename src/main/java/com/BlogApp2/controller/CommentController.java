package com.BlogApp2.controller;

import com.BlogApp2.dto.request.CommentRequest;
import com.BlogApp2.dto.response.ApiResponse;
import com.BlogApp2.dto.response.CommentResponse;
import com.BlogApp2.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
//nested under posts (/api/posts/{postId}/comments),
// and here's the reasoning: a comment is intrinsically tied to a post —
// it never makes sense to fetch "comment #47" without already being in the context of which post you're viewing,
// and creating a comment always requires knowing which post it belongs to.
// Nesting the route reflects that real dependency in the URL structure itself,
// which is more RESTful than a flat /api/comments?postId=... that treats comments as if they existed independently.
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable UUID postId,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse created = commentService.createComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(@PathVariable UUID postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(ApiResponse.success("Comments retrieved successfully", comments));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable UUID postId,
            @PathVariable UUID commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}