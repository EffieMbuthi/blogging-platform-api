package com.BlogApp2.controller;

import com.BlogApp2.dto.request.ReviewRequest;
import com.BlogApp2.dto.response.ApiResponse;
import com.BlogApp2.dto.response.ReviewResponse;
import com.BlogApp2.service.ReviewService;
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
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable UUID postId,
            @Valid @RequestBody ReviewRequest request) {
        ReviewResponse created = reviewService.createReview(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByPost(@PathVariable UUID postId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByPost(postId);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID postId,
            @PathVariable UUID reviewId) {
        reviewService.deleteReview(postId, reviewId);
        return ResponseEntity.noContent().build();
    }
}