package com.BlogApp2.service;

import com.BlogApp2.dto.request.PostRequest;
import com.BlogApp2.dto.response.PostDetailDto;
import com.BlogApp2.dto.response.PostSummaryDto;

import java.util.List;
import java.util.UUID;

public interface PostService {
    PostDetailDto createPost(PostRequest request);
    PostDetailDto getPostById(UUID id);
    List<PostSummaryDto> getAllPosts();
    PostDetailDto updatePost(UUID id, PostRequest request);
    void deletePost(UUID id);
}