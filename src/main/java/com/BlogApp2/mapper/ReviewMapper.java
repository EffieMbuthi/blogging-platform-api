package com.BlogApp2.mapper;

import com.BlogApp2.dto.response.ReviewResponse;
import com.BlogApp2.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final AuthorMapper authorMapper;

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse (
                review.getId(),
                authorMapper.toAuthorSummary(review.getUser()),
                review.getRating(),
                review.getCreatedAt()
        );
    }
}