package com.BlogApp2.graphql;

import com.BlogApp2.dto.request.ReviewRequest;
import com.BlogApp2.dto.response.ReviewResponse;
import com.BlogApp2.service.ReviewService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReviewGraphQLController {

    private final ReviewService reviewService;
    private final Validator validator;

    @QueryMapping
    public List<ReviewResponse> getReviewsByPost(@Argument UUID postId) {
        return reviewService.getReviewsByPost(postId);
    }

    @MutationMapping
    public ReviewResponse createReview(@Argument UUID postId, @Argument("input") ReviewRequest input) {
        // CreateReviewInput only carries userId and rating, postId arrives as its own
        // schema-mandated non-null argument, so it has to be filled in here before
        // validating, rather than via @Valid on the parameter directly.
        input.setPostId(postId);

        Set<ConstraintViolation<ReviewRequest>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return reviewService.createReview(postId, input);
    }

    @MutationMapping
    public Boolean deleteReview(@Argument UUID postId, @Argument UUID reviewId) {
        reviewService.deleteReview(postId, reviewId);
        return true;
    }
}
