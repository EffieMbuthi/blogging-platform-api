package com.BlogApp2.controller;

import com.BlogApp2.dto.request.PostRequest;
import com.BlogApp2.dto.response.ApiResponse;
import com.BlogApp2.dto.response.PostDetailDto;
import com.BlogApp2.dto.response.PostSummaryDto;
import com.BlogApp2.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Endpoints for creating, reading, updating, and deleting blog posts")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Create a new blog post",
            description = "Creates a post authored by an existing user. Tags are supplied by name and are " +
                    "automatically reused if they already exist, or created if they don't."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Post created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Author (user) not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed (e.g. blank title/body)")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<PostDetailDto>> createPost(@Valid @RequestBody PostRequest request) {
        PostDetailDto created = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post created successfully", created));
    }

    @Operation(
            summary = "Get a single post by id",
            description = "Returns full post detail, including body, tags, comments, and review stats."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No post exists with the given id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailDto>> getPostById(@PathVariable UUID id) {
        PostDetailDto post = postService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success("Post retrieved successfully", post));
    }

    @Operation(
            summary = "List posts (paginated, sortable, searchable)",
            description = "Returns a page of lightweight post summaries. The optional `search` parameter matches " +
                    "posts whose title starts with the given text (case-insensitive), which is what allows this " +
                    "lookup to use a database index rather than scanning every row. It does not match text " +
                    "appearing in the middle of a title. Also supports standard `page`, `size`, and `sort` query parameters."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostSummaryDto>>> getAllPosts(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostSummaryDto> posts = postService.getAllPosts(search, pageable);
        return ResponseEntity.ok(ApiResponse.success("Posts retrieved successfully", posts));
    }

    @Operation(
            summary = "Update an existing post",
            description = "Replaces the post's title, body, and tag set. Authorship cannot be changed via this endpoint."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No post exists with the given id"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailDto>> updatePost(
            @PathVariable UUID id, @Valid @RequestBody PostRequest request) {
        PostDetailDto updated = postService.updatePost(id, request);
        return ResponseEntity.ok(ApiResponse.success("Post updated successfully", updated));
    }


    @Operation(
            summary = "Delete a post",
            description = "Permanently deletes a post. Does not cascade-delete associated comments/reviews unless configured at the DB level."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No post exists with the given id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}