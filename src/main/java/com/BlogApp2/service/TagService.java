package com.BlogApp2.service;

import com.BlogApp2.dto.request.TagRequest;
import com.BlogApp2.dto.response.TagResponse;

import java.util.List;
import java.util.UUID;

public interface TagService {
    TagResponse createTag(TagRequest request);
    List<TagResponse> getAllTags();
    TagResponse getTagById(UUID id);
    TagResponse updateTag(UUID id, TagRequest request);
    void deleteTag(UUID id);
}