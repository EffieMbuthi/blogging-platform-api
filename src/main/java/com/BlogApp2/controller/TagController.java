package com.BlogApp2.controller;

import com.BlogApp2.dto.response.TagResponse;
import com.BlogApp2.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public List<TagResponse> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public TagResponse getTagById(@PathVariable UUID id) {
        return tagService.getTagById(id);
    }
}