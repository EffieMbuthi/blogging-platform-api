package com.BlogApp2.graphql;

import com.BlogApp2.dto.request.TagRequest;
import com.BlogApp2.dto.response.TagResponse;
import com.BlogApp2.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Validated
public class TagGraphQLController {

    private final TagService tagService;

    @QueryMapping
    public TagResponse getTag(@Argument UUID id) {
        return tagService.getTagById(id);
    }

    @QueryMapping
    public List<TagResponse> getAllTags() {
        return tagService.getAllTags();
    }

    @MutationMapping
    public TagResponse createTag(@Argument @Valid TagRequest input) {
        return tagService.createTag(input);
    }

    @MutationMapping
    public TagResponse updateTag(@Argument UUID id, @Argument @Valid TagRequest input) {
        return tagService.updateTag(id, input);
    }

    @MutationMapping
    public Boolean deleteTag(@Argument UUID id) {
        tagService.deleteTag(id);
        return true;
    }
}
