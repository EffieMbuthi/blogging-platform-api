package com.BlogApp2.service.impl;

import com.BlogApp2.dto.request.PostRequest;
import com.BlogApp2.dto.response.PostDetailDto;
import com.BlogApp2.dto.response.PostSummaryDto;
import com.BlogApp2.exception.PostNotFoundException;
import com.BlogApp2.exception.UserNotFoundException;
import com.BlogApp2.mapper.PostMapper;
import com.BlogApp2.model.Post;
import com.BlogApp2.model.Tag;
import com.BlogApp2.model.User;
import com.BlogApp2.repository.CommentRepository;
import com.BlogApp2.repository.PostCommentCountProjection;
import com.BlogApp2.repository.PostRepository;
import com.BlogApp2.repository.PostTagProjection;
import com.BlogApp2.repository.TagRepository;
import com.BlogApp2.repository.UserRepository;
import com.BlogApp2.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostDetailDto createPost(PostRequest request) {

        // Step A: verify the author exists, fail fast before any writes
        User author = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        // Step B: resolve each tag name to a persisted Tag entity (find-or-create)
        Set<Tag> tags = request.getTagNames().stream()
                .map(this::resolveTag)
                .collect(Collectors.toSet());

        // Step C: build and save the post
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setBody(request.getBody());
        post.setUser(author);
        post.setTags(tags);

        Post savedPost = postRepository.save(post);

        return postMapper.toDetailDto(savedPost);
    }

    private Tag resolveTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(name);
                    return tagRepository.save(newTag);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailDto getPostById(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return postMapper.toDetailDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostSummaryDto> getAllPosts(String search, Pageable pageable) {
        Page<Post> posts = (search == null || search.isBlank())
                ? postRepository.findAll(pageable)
                : postRepository.findByTitleStartingWithIgnoreCase(search, pageable);

        List<UUID> postIds = posts.getContent().stream().map(Post::getId).toList();

        if (postIds.isEmpty()) {
            return posts.map(post -> postMapper.toSummaryDto(post, Set.of(), 0L));
        }

        // Two queries for the whole page, not two queries per post: this is what actually
        // fixes the N+1 pattern, the author itself no longer needs a separate query per post
        // either, because PostRepository's @EntityGraph already joined it into the page query.
        Map<UUID, Set<String>> tagNamesByPostId = postRepository.findTagNamesForPostIds(postIds).stream()
                .collect(Collectors.groupingBy(
                        PostTagProjection::getPostId,
                        Collectors.mapping(PostTagProjection::getTagName, Collectors.toSet())));

        Map<UUID, Long> commentCountByPostId = commentRepository.countByPostIds(postIds).stream()
                .collect(Collectors.toMap(PostCommentCountProjection::getPostId, PostCommentCountProjection::getCommentCount));

        return posts.map(post -> postMapper.toSummaryDto(
                post,
                tagNamesByPostId.getOrDefault(post.getId(), Set.of()),
                commentCountByPostId.getOrDefault(post.getId(), 0L)));
    }


    //request.getUserId() is never called. Whatever you type into that field in Postman —
    // the real user's id, a completely different random UUID, garbage text, doesn't matter —
    // this method never looks at it. (## You dont want to change the userId, you want the current user to never be changed once created.)
    @Override
    @Transactional
    public PostDetailDto updatePost(UUID id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        post.setTitle(request.getTitle());
        post.setBody(request.getBody());

        Set<Tag> tags = request.getTagNames().stream()
                .map(this::resolveTag)
                .collect(Collectors.toSet());
        post.setTags(tags);

        Post updatedPost = postRepository.save(post);
        return postMapper.toDetailDto(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(UUID id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException(id);
        }
        postRepository.deleteById(id);
    }
}