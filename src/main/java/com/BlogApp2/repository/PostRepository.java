package com.BlogApp2.repository;

import com.BlogApp2.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUserId(UUID userId);
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}