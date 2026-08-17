package com.BlogApp2.repository;

import com.BlogApp2.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUserId(UUID userId);

    // Overrides the inherited findAll(Pageable) purely to add the entity graph below,
    // so the author comes back in the same query as the page, not one query per post.
    @EntityGraph(attributePaths = "user")
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Post> findByTitleStartingWithIgnoreCase(String titlePrefix, Pageable pageable);

    @Query("SELECT p.id AS postId, t.name AS tagName FROM Post p JOIN p.tags t WHERE p.id IN :postIds")
    List<PostTagProjection> findTagNamesForPostIds(@Param("postIds") List<UUID> postIds);
}
