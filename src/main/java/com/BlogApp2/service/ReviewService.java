package com.BlogApp2.service;

import com.BlogApp2.dto.request.ReviewRequest;
import com.BlogApp2.dto.response.ReviewResponse;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ReviewResponse createReview(UUID postId, ReviewRequest request);
    List<ReviewResponse> getReviewsByPost(UUID postId);
    void deleteReview(UUID postId, UUID reviewId);
}