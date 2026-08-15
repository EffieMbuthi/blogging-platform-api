package com.BlogApp2.service.impl;

import com.BlogApp2.dto.request.ReviewRequest;
import com.BlogApp2.dto.response.ReviewResponse;
import com.BlogApp2.exception.*;
import com.BlogApp2.mapper.ReviewMapper;
import com.BlogApp2.model.Post;
import com.BlogApp2.model.Review;
import com.BlogApp2.model.User;
import com.BlogApp2.repository.PostRepository;
import com.BlogApp2.repository.ReviewRepository;
import com.BlogApp2.repository.UserRepository;
import com.BlogApp2.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(UUID postId, ReviewRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        reviewRepository.findByPostIdAndUserId(postId, user.getId())
                .ifPresent(existing -> {
                    throw new DuplicateReviewException(postId, user.getId());
                });

        Review review = new Review();
        review.setPost(post);
        review.setUser(user);
        review.setRating(request.getRating());

        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByPost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        return reviewRepository.findByPostId(postId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteReview(UUID postId, UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if (!review.getPost().getId().equals(postId)) {
            throw new ReviewPostMismatchException(postId, review.getUser().getId());
        }

        reviewRepository.deleteById(reviewId);
    }
}