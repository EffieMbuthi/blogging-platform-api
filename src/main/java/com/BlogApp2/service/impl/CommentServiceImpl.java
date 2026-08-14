package com.BlogApp2.service.impl;

import com.BlogApp2.dto.request.CommentRequest;
import com.BlogApp2.dto.response.CommentResponse;
import com.BlogApp2.exception.CommentNotFoundException;
import com.BlogApp2.exception.CommentPostMismatchException;
import com.BlogApp2.exception.PostNotFoundException;
import com.BlogApp2.exception.UserNotFoundException;
import com.BlogApp2.mapper.CommentMapper;
import com.BlogApp2.model.Comment;
import com.BlogApp2.model.Post;
import com.BlogApp2.model.User;
import com.BlogApp2.repository.CommentRepository;
import com.BlogApp2.repository.PostRepository;
import com.BlogApp2.repository.UserRepository;
import com.BlogApp2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponse createComment(UUID postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        User author = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        Comment comment = new Comment();
        comment.setBody(request.getBody());
        comment.setPost(post);
        comment.setUser(author);

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteComment(UUID postId, UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        //ownership check — comment.getPost().getId().equals(postId)
        // is exactly the guard flagged when the controller is designed:
        // it fetches the comment by its own id first, then cross-checks that its parent post matches what's in the URL.
        // Notice this only works cleanly because comment.getPost() gives you the actual Post object (not just an id)
        // — this is the object-graph navigation from the earlier lazy-loading conversation, paying off again here.
        if (!comment.getPost().getId().equals(postId)) { //the URL path (/api/posts/{postId}/comments
            throw new CommentPostMismatchException(commentId, postId);
        }

        commentRepository.deleteById(commentId);
    }
}