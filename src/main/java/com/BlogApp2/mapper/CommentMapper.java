package com.BlogApp2.mapper;

import com.BlogApp2.dto.response.CommentResponse;
import com.BlogApp2.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final AuthorMapper authorMapper;

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getBody(),
                authorMapper.toAuthorSummary(comment.getUser()),
                comment.getCreatedAt()
        );
    }
}