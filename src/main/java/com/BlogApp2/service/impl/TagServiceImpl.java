package com.BlogApp2.service.impl;

import com.BlogApp2.dto.request.TagRequest;
import com.BlogApp2.dto.response.TagResponse;
import com.BlogApp2.exception.DuplicateTagException;
import com.BlogApp2.exception.TagInUseException;
import com.BlogApp2.exception.TagNotFoundException;
import com.BlogApp2.mapper.TagMapper;
import com.BlogApp2.model.Tag;
import com.BlogApp2.repository.TagRepository;
import com.BlogApp2.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    @Transactional
    public TagResponse createTag(TagRequest request) {
        if (tagRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateTagException(request.getName());
        }

        Tag tag = tagMapper.toEntity(request);
        Tag savedTag = tagRepository.save(tag);

        return tagMapper.toResponse(savedTag);
    }

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

    @Override
    @Transactional
    public TagResponse updateTag(UUID id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        tagRepository.findByName(request.getName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateTagException(request.getName());
                });

        tag.setName(request.getName());
        Tag updatedTag = tagRepository.save(tag);

        return tagMapper.toResponse(updatedTag);
    }

    @Override
    @Transactional
    public void deleteTag(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        if (!tag.getPosts().isEmpty()) {
            throw new TagInUseException(id);
        }

        tagRepository.deleteById(id);
    }
}