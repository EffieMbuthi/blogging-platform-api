package com.BlogApp2.repository;

import java.util.UUID;

/**
 * One row per post-tag pairing, used to fetch every tag name for a whole
 * page of posts in a single query instead of once per post.
 */
public interface PostTagProjection {
    UUID getPostId();
    String getTagName();
}
