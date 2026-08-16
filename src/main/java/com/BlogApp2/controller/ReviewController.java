package com.BlogApp2.controller;

import com.BlogApp2.dto.request.ReviewRequest;
import com.BlogApp2.dto.response.ApiResponse;
import com.BlogApp2.dto.response.ReviewResponse;
import com.BlogApp2.service.ReviewService;
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
@RequestMapping("/api/posts/{postId}/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Endpoints for rating a post; one review per user per post")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Submit a review for a post", description = "A user may only review a given post once.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Review created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post or user not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User has already reviewed this post"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed (e.g. rating out of range)")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable UUID postId,
            @Valid @RequestBody ReviewRequest request) {
        ReviewResponse created = reviewService.createReview(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review created successfully", created));
    }

    @Operation(summary = "Get all reviews for a post")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No post exists with the given id")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByPost(@PathVariable UUID postId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByPost(postId);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }

    @Operation(summary = "Delete a review from a post")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No review exists with the given id"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Review exists but does not belong to the specified post")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID postId,
            @PathVariable UUID reviewId) {
        reviewService.deleteReview(postId, reviewId);
        return ResponseEntity.noContent().build();
    }
}