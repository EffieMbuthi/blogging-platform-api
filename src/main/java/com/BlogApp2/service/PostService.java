package com.BlogApp2.service;

import com.BlogApp2.dto.request.PostRequest;
import com.BlogApp2.dto.response.PostDetailDto;
import com.BlogApp2.dto.response.PostSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PostService {
    PostDetailDto createPost(PostRequest request);
    PostDetailDto getPostById(UUID id);
    Page<PostSummaryDto> getAllPosts(String search, Pageable pageable);
    PostDetailDto updatePost(UUID id, PostRequest request);
    void deletePost(UUID id);
}