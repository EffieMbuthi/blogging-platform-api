package com.BlogApp2.controller;

import com.BlogApp2.dto.response.ApiResponse;
import com.BlogApp2.dto.response.TagResponse;
import com.BlogApp2.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tags", description = "Read-only endpoints for browsing tags; tags are created implicitly via posts")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "List all tags")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ResponseEntity.ok(ApiResponse.success("Tags retrieved successfully", tags));
    }

    @Operation(summary = "Get a tag by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> getTagById(@PathVariable UUID id) {
        TagResponse tag = tagService.getTagById(id);
        return ResponseEntity.ok(ApiResponse.success("Tag retrieved successfully", tag));
    }
}