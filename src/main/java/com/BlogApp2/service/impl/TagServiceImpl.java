package com.BlogApp2.service.impl;

import com.BlogApp2.dto.response.TagResponse;
import com.BlogApp2.exception.TagNotFoundException;
import com.BlogApp2.mapper.TagMapper;
import com.BlogApp2.model.Tag;
import com.BlogApp2.repository.TagRepository;
import com.BlogApp2.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toResponse)
                .toList();
    }

    @Override
    public TagResponse getTagById(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
        return tagMapper.toResponse(tag);
    }
}