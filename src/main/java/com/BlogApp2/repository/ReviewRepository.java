package com.BlogApp2.repository;

import com.BlogApp2.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findByPostIdAndUserId(UUID postId, UUID userId);
    List<Review> findByPostId(UUID postId);
}