package com.BlogApp2.repository;

import java.util.UUID;

/**
 * One row per post, holding its comment count, used to compute every post's
 * comment count in a whole page with a single grouped query instead of one
 * query per post.
 */
public interface PostCommentCountProjection {
    UUID getPostId();
    Long getCommentCount();
}
