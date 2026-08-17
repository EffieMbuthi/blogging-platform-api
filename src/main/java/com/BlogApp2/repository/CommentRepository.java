package com.BlogApp2.repository;

import com.BlogApp2.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPostId(UUID postId);

    @Query("SELECT c.post.id AS postId, COUNT(c) AS commentCount FROM Comment c WHERE c.post.id IN :postIds GROUP BY c.post.id")
    List<PostCommentCountProjection> countByPostIds(@Param("postIds") List<UUID> postIds);
}
