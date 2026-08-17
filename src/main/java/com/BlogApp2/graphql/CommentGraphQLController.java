package com.BlogApp2.graphql;

import com.BlogApp2.dto.request.CommentRequest;
import com.BlogApp2.dto.response.CommentResponse;
import com.BlogApp2.service.CommentService;
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
public class CommentGraphQLController {

    private final CommentService commentService;

    @QueryMapping
    public List<CommentResponse> getCommentsByPost(@Argument UUID postId) {
        return commentService.getCommentsByPost(postId);
    }

    @MutationMapping
    public CommentResponse createComment(@Argument UUID postId, @Argument @Valid CommentRequest input) {
        return commentService.createComment(postId, input);
    }

    @MutationMapping
    public CommentResponse updateComment(
            @Argument UUID postId, @Argument UUID commentId, @Argument @Valid CommentRequest input) {
        return commentService.updateComment(postId, commentId, input);
    }

    @MutationMapping
    public Boolean deleteComment(@Argument UUID postId, @Argument UUID commentId) {
        commentService.deleteComment(postId, commentId);
        return true;
    }
}
