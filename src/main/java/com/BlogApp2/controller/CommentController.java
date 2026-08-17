package com.BlogApp2.controller;

import com.BlogApp2.dto.request.CommentRequest;
import com.BlogApp2.dto.response.ApiResponse;
import com.BlogApp2.dto.response.CommentResponse;
import com.BlogApp2.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Comments", description = "Endpoints for creating, reading, and deleting comments on a specific post")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Add a comment to a post",
            description = "Creates a comment authored by an existing user, attached to the post identified in the URL path."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post or author (user) not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed (e.g. blank body)")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable UUID postId,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse created = commentService.createComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment created successfully", created));
    }

    @Operation(
            summary = "Get all comments for a post",
            description = "Returns every comment attached to the post identified in the URL path."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No post exists with the given id")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(@PathVariable UUID postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(ApiResponse.success("Comments retrieved successfully", comments));
    }

    @Operation(
            summary = "Update a comment on a post",
            description = "Updates a comment's body, but only if it actually belongs to the post identified in the URL path. Author cannot be changed."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No comment exists with the given id"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Comment exists but does not belong to the specified post"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable UUID postId,
            @PathVariable UUID commentId,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse updated = commentService.updateComment(postId, commentId, request);
        return ResponseEntity.ok(ApiResponse.success("Comment updated successfully", updated));
    }

    @Operation(
            summary = "Delete a comment from a post",
            description = "Deletes a specific comment, but only if it actually belongs to the post identified in the URL path."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No comment exists with the given id"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Comment exists but does not belong to the specified post")// CommentPostMismatchException
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable UUID postId,
            @PathVariable UUID commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}