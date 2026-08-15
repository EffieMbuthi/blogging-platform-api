package com.BlogApp2.service;

import com.BlogApp2.dto.response.TagResponse;

import java.util.List;
import java.util.UUID;

public interface TagService {
    List<TagResponse> getAllTags();
    TagResponse getTagById(UUID id);
}