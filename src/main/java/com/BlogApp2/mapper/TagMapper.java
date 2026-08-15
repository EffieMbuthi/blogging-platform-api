package com.BlogApp2.mapper;

import com.BlogApp2.dto.response.TagResponse;
import com.BlogApp2.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public TagResponse toResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}