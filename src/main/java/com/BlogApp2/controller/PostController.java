package com.BlogApp2.controller;

import com.BlogApp2.dto.request.PostRequest;
import com.BlogApp2.dto.response.ApiResponse;
import com.BlogApp2.dto.response.PostDetailDto;
import com.BlogApp2.dto.response.PostSummaryDto;
import com.BlogApp2.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostDetailDto>> createPost(@Valid @RequestBody PostRequest request) {
        PostDetailDto created = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailDto>> getPostById(@PathVariable UUID id) {
        PostDetailDto post = postService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success("Post retrieved successfully", post));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostSummaryDto>>> getAllPosts() {
        List<PostSummaryDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.success("Posts retrieved successfully", posts));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailDto>> updatePost(
            @PathVariable UUID id, @Valid @RequestBody PostRequest request) {
        PostDetailDto updated = postService.updatePost(id, request);
        return ResponseEntity.ok(ApiResponse.success("Post updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}